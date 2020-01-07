
DROP FUNCTION IF EXISTS fn_increment_stock(p_id INTEGER) ;
CREATE OR REPLACE FUNCTION fn_increment_stock(p_id INTEGER)
   RETURNS INTEGER AS $$
DECLARE
  rec_inspection   RECORD; -- store cursor data
  v_result INTEGER DEFAULT -1; -- return value, not processed, 0 successful, 1 error
  v_facilityid INTEGER; -- facility id of Central store
  v_productid INTEGER; -- product in inspection
  v_effective_date TIMESTAMP WITHOUT TIME ZONE; -- timestamp for new record
  v_note text; -- note
  v_cnt INTEGER; -- record counter
  v_lotflag BOOLEAN; -- record counter

  -- variable to store indivial data fields
  v_stock_card_id INTEGER;
  v_total_quantity INTEGER;
  v_lot_id INTEGER;
  v_lot_onhand_id INTEGER;
  v_lot_quantity INTEGER;
  v_old_productid INTEGER;

  -- ge inspection records. Status must be DRAFT to process. must be sorted by productid
  cur_inspection CURSOR(p_insid INTEGER)
    	FOR select i.productid, i.passquantity totalquantity, t.id lotid, it.lotnumber, it.passquantity, i.lotflag
					from inspections n
					join inspection_line_items i on n.id = i.inspectionid
					left join inspection_lots it on i.id = it.inspectionlineitemid
					left join lots t on t.lotnumber = it.lotnumber
					where n.id = p_insid  order by productid;
BEGIN
    v_cnt := 0;
    v_facilityid := (SELECT f.id from facilities f join facility_types ft ON f.typeId = ft.id where lower(ft.code) = lower('CVS') limit 1); -- TODO: get from db
    v_note := 'update by system on ' || NOW()::TEXT;
    v_effective_date := NOW();
    v_old_productid := -1; -- to determine change in product in inspection recordset

	  OPEN cur_inspection(p_id);
    LOOP
    -- fetch row into the film
      FETCH cur_inspection INTO rec_inspection;
    -- exit when no more row to fetch
      EXIT WHEN NOT FOUND;
       -- reset counter for new product and get total passed quantity
      IF v_old_productid != rec_inspection.productid THEN
       v_cnt := 0;
       -- get aggregate quantity from all product lots in rec set
	     v_total_quantity= COALESCE((select sum(COALESCE(it.passquantity,0)) totalpassquantity from inspections n  join inspection_line_items i on n.id = i.inspectionid left join inspection_lots it on i.id = it.inspectionlineitemid left join lots t on t.lotnumber = it.lotnumber where n.id = p_id and i.productid = rec_inspection.productid LIMIT 1),0);
     END IF;
      -- store fetech record into individual variable
      v_cnt := v_cnt + 1;
      v_productid := rec_inspection.productid;
      v_lot_id := rec_inspection.lotid;
      v_lot_quantity := rec_inspection.passquantity;
      v_lotflag := rec_inspection.lotflag;
	    v_stock_card_id= COALESCE((SELECT id FROM stock_cards WHERE productid = v_productid and facilityid = v_facilityid LIMIT 1),0);
      -- create stock card if not exist otherwise update
			IF v_cnt = 1 THEN
        -- create stok card if not exists
				IF v_stock_card_id = 0 THEN
					INSERT INTO stock_cards(
	              facilityid, productid, v_total_quantity, effectivedate, notes)
						VALUES (v_facilityid, v_productid, v_total_quantity, v_effectivedate, v_note);
				v_stock_card_id= COALESCE((SELECT id FROM stock_cards WHERE productid = v_productid and facilityid = v_facilityid LIMIT 1),0);
				ELSE
          -- increment product stock onhand by passed quantity from all lots
					UPDATE stock_cards
					SET totalquantityonhand= totalquantityonhand + v_total_quantity, notes=v_note
					WHERE id = v_stock_card_id;
				END IF;
			END IF;
     -- increment lots on hand only if product has lot
     IF v_lotflag = 't' THEN
     -- create or update lots on hand
     v_lot_onhand_id= COALESCE((select id from lots_on_hand where stockcardid = v_stock_card_id and lotid = rec_inspection.lotid LIMIT 1),0);

			IF v_lot_onhand_id = 0 THEN
				INSERT INTO lots_on_hand(
					stockcardid, lotid, quantityonhand)
				VALUES (v_stock_card_id, v_lot_id, COALESCE(v_lot_quantity,0));
				v_lot_onhand_id= COALESCE((select id from lots_on_hand where stockcardid = v_stock_card_id and lotid = rec_inspection.lotid LIMIT 1),0);

      ELSE
      UPDATE lots_on_hand
					SET quantityonhand= COALESCE(quantityonhand,0) + COALESCE(v_lot_quantity,0)
				WHERE id = v_lot_onhand_id;
 			END IF;

      -- always insert stock card entries
      INSERT INTO stock_card_entries(
				stockcardid, lotonhandid, type, quantity, notes, occurred)
			VALUES (v_stock_card_id, v_lot_onhand_id, 'CREDIT', v_lot_quantity, v_note, NOW());
			v_old_productid :=  rec_inspection.productid;
      END IF;
      v_result:=0;
		END LOOP;
   -- Close the cursor
   CLOSE cur_inspection;
   -- change inspection status
   IF v_result = 0 THEN
     UPDATE inspections set status = 'RELEASED' WHERE id = p_id;
	 END IF;

   RETURN v_result;
EXCEPTION WHEN OTHERS THEN
	begin
		v_result:=1;
		return v_result;
	end;
END; $$

LANGUAGE plpgsql;



--Add seed location data


INSERT INTO public.location_types(
             code, name, description, active, createdby, createddate,
            modifiedby, modifieddate)
    VALUES ('TYPECO3', 'TYPEW', 'DESCRS', TRUE, 1, NOW(),
            1, NOW());


            INSERT INTO public.locations(
             code, name, active, typeid, zoneid, size, capacity, aisleid,
            parentid, createdby, createddate, modifiedby, modifieddate, scrap)
    VALUES ( '400L', '400L', true, 1, 1, 100, 3000, 2000,
            NULL, 1,  NOW(), 1, now(), true);











