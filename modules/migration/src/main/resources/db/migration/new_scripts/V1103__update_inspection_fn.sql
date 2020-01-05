ALTER TABLE inspection_lots
    DROP COLUMN IF EXISTS expirydate;


ALTER TABLE inspection_lots
    ADD COLUMN expirydate timestamp without time zone;



ALTER TABLE inspection_lots
    DROP COLUMN IF EXISTS receivedQuantity;


ALTER TABLE inspection_lots
    ADD COLUMN receivedQuantity INTEGER;


ALTER TABLE inspection_line_items
    DROP COLUMN IF EXISTS noCoolantFlag;

    ALTER TABLE inspection_line_items
    ADD COLUMN noCoolantFlag boolean default false;

    --Add VVM Status Table

    DROP TABLE IF EXISTS public.vvm_statuses;

    CREATE TABLE public.vvm_statuses
    (
      id serial,
      name character varying(250),
      CONSTRAINT vvm_statuses_pkey PRIMARY KEY (id)
    );

   INSERT INTO vvm_statuses(NAME) VALUES('VVM1'),('VVM2'),('VVM3'),('VVM4');


    --Function




 DROP FUNCTION IF EXISTS fn_create_inpsection(p_id INTEGER) ;
CREATE OR REPLACE FUNCTION fn_create_inpsection(p_id INTEGER)
   RETURNS INTEGER AS $$
DECLARE
  rec_receive   RECORD; -- store cursor data
  v_result INTEGER DEFAULT -1; -- return value, not processed, 0 successful, 1 error
  v_facilityid INTEGER; -- facility id of Central store
  v_productid INTEGER; -- product in receive
  v_effective_date TIMESTAMP WITHOUT TIME ZONE; -- timestamp for new record
  v_note text; -- note
  v_cnt INTEGER; -- record counter
  v_lotflag BOOLEAN; -- record counter

  -- variable to store indivial data fields
  v_inspectonid INTEGER;
  v_inspectonlineitemid INTEGER;
  v_receiveid INTEGER;
  v_total_quantity INTEGER;
  v_total_box_quantity INTEGER;
  v_lot_id INTEGER;
  v_lot_number VARCHAR(255);
  v_lot_onhand_id INTEGER;
  v_lot_quantity INTEGER;
  v_box_quantity INTEGER;
  v_old_productid INTEGER;
  v_expirydate DATE;
  v_received_quantity Integer;

  -- ge receive records. Status must be DRAFT to process. must be sorted by productid
  cur_receive CURSOR(p_insid INTEGER)
    	FOR select r.id, r.id receiveid, rli.productid, rli.quantitycounted, rli.boxcounted,  t.id lotid, rl.lotnumber, rli.lotflag, rl.expirydate, rl.quantity
					from receives r
					join receive_line_items rli on r.id = rli.receiveid
					left join receive_lots rl on rli.id = rl.receivelineitemid
					left join lots t on t.lotnumber = rl.lotnumber
					where r.id = p_id and status = 'DRAFT' order by productid;

BEGIN
    v_cnt := 0;
        v_facilityid := (SELECT f.id from facilities f join facility_types ft ON f.typeId = ft.id where lower(ft.code) = lower('CVS') limit 1); -- TODO: get from db
    v_note := 'update by system on ' || NOW()::TEXT;
    v_effective_date := NOW();
    v_old_productid := -1; -- to determine change in product in receive recordset

	  OPEN cur_receive(p_id);
    LOOP
    -- fetch row into the film
      FETCH cur_receive INTO rec_receive;
    -- exit when no more row to fetch
      EXIT WHEN NOT FOUND;
       -- reset counter for new product and get total passed quantity
      IF v_old_productid != rec_receive.productid THEN
       v_cnt := 0;
        -- get aggregate quantity from all product lots in rec set
       select sum(COALESCE(rli.quantitycounted,0)) totalquantity, sum(COALESCE(rli.boxcounted,0)) totalboxes into v_total_quantity, v_total_box_quantity from receives r join receive_line_items rli on r.id = rli.receiveid  left join receive_lots rl on rli.id = rl.receivelineitemid  left join lots t on t.lotnumber = rl.lotnumber  where r.id = rec_receive.receiveid and rli.productid = rec_receive.productid LIMIT 1;

       INSERT INTO inspections(
			  receiveid, status, createddate, modifieddate)
				VALUES (rec_receive.receiveid, 'DRAFT',NOW(),NOW());
       v_inspectonid= COALESCE((select id from inspections where receiveid = p_id LIMIT 1),0);

			 INSERT INTO inspection_line_items(
				inspectionid, productid, quantitycounted, boxcounted, lotflag, createddate, modifieddate)
				VALUES (v_inspectonid, rec_receive.productid, v_total_quantity, v_total_box_quantity, rec_receive.lotflag, NOW(), NOW());

       v_inspectonlineitemid= COALESCE((select id from inspection_line_items where inspectionid = v_inspectonid and productid = rec_receive.productid LIMIT 1),0);

 	    END IF;
      -- store fetech record into individual variable
      v_cnt := v_cnt + 1;
      v_receiveid := rec_receive.id;
      v_productid := rec_receive.productid;
      v_lot_number := rec_receive.lotnumber;
      v_lot_quantity := rec_receive.quantitycounted;
      v_box_quantity := rec_receive.boxcounted;
      v_lotflag := rec_receive.lotflag;
      v_expirydate := rec_receive.expirydate;
      v_received_quantity := rec_receive.quantity;


			INSERT INTO inspection_lots(
			 inspectionlineitemid, lotnumber, countedquantity, createddate, modifieddate,expirydate,receivedQuantity)
			VALUES (v_inspectonlineitemid, v_lot_number, v_lot_quantity, NOW(), NOW(),v_expirydate,v_received_quantity);
      v_old_productid :=  rec_receive.productid;
		END LOOP;
    v_result:=0;
   -- Close the cursor
   CLOSE cur_receive;

   IF v_result = 0 THEN
     UPDATE receives set status = 'RECEIVED' WHERE id = p_id;
	 END IF;
   RETURN v_result;
EXCEPTION WHEN OTHERS THEN
	begin
		v_result:=1;
		return v_result;
	end;
END; $$

LANGUAGE plpgsql;



--Increment Stock Cards


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
					where n.id = p_insid and status = 'DRAFT' order by productid;
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

