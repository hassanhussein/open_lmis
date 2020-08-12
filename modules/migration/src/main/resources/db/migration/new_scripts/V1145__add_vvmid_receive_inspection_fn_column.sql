 ALTER TABLE public.receive_lots
    ADD COLUMN IF NOT EXISTS vvmid integer;



    -- FUNCTION: public.fn_create_inpsection(integer)

-- DROP FUNCTION public.fn_create_inpsection(integer);

CREATE OR REPLACE FUNCTION public.fn_create_inpsection(
	p_id integer)
    RETURNS integer
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE
AS $BODY$DECLARE
rec_receive RECORD; -- store cursor data
v_result INTEGER DEFAULT -1; -- return value, not processed, 0 successful, 1 error
v_facilityid INTEGER; -- facility id of Central store
v_productid INTEGER; -- product in receive
v_effective_date TIMESTAMP WITHOUT TIME ZONE; -- timestamp for new record
v_note text; -- note
v_cnt INTEGER; -- record counter
v_lotflag BOOLEAN; -- record counter
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
v_invoiceNumber varchar(255);
v_boxNumber Integer;
v_location_id integer;
v_vvm_id integer;
cur_receive CURSOR(p_insid INTEGER)
FOR select r.receiveNumber, r.id,r.note,rl.vvmId,rl.vvmid, r.description,r.blawbnumber, r.id receiveid, rli.productid, rli.quantitycounted,rl.boxNumber,rl.locationId, rli.boxcounted,r.invoiceNumber, t.id lotid, rl.lotnumber, rli.lotflag, rl.expirydate, rl.quantity
from receives r
join receive_line_items rli on r.id = rli.receiveid
left join receive_lots rl on rli.id = rl.receivelineitemid
left join lots t on t.lotnumber = rl.lotnumber
where r.id = p_id and status = 'RECEIVED' order by productid;
BEGIN
v_cnt := 0;
v_facilityid := (SELECT f.id from facilities f join facility_types ft ON f.typeId = ft.id where lower(ft.code) = lower('CVS') limit 1); -- TODO: get from db
v_note := 'update by system on ' || NOW()::TEXT;
v_effective_date := NOW();
v_old_productid := -1; -- to determine change in product in receive recordset
OPEN cur_receive(p_id);
LOOP
FETCH cur_receive INTO rec_receive;
EXIT WHEN NOT FOUND;
IF v_old_productid != rec_receive.productid THEN
v_cnt := 0;
select sum(COALESCE(rli.quantitycounted,0)) totalquantity, sum(COALESCE(rli.boxcounted,0)) totalboxes into v_total_quantity, v_total_box_quantity from receives r join receive_line_items rli on r.id = rli.receiveid left join receive_lots rl on rli.id = rl.receivelineitemid left join lots t on t.lotnumber = rl.lotnumber where r.id = rec_receive.receiveid and rli.productid = rec_receive.productid LIMIT 1;
INSERT INTO inspections(
receiveid, status, createddate, modifieddate,varNumber,invoiceNumber,inspectionnote, descriptionofinspection,receiptnumber)
VALUES (rec_receive.receiveid, 'DRAFT',NOW(),NOW(),rec_receive.receiveNumber,rec_receive.invoiceNumber, rec_receive.note,rec_receive.description,rec_receive.blawbnumber);
v_inspectonid= COALESCE((select id from inspections where receiveid = p_id LIMIT 1),0);
INSERT INTO inspection_line_items(
inspectionid, productid, quantitycounted, boxcounted, lotflag, createddate, modifieddate)
VALUES (v_inspectonid, rec_receive.productid, v_total_quantity, v_total_box_quantity, rec_receive.lotflag, NOW(), NOW());
v_inspectonlineitemid= COALESCE((select id from inspection_line_items where inspectionid = v_inspectonid and productid = rec_receive.productid LIMIT 1),0);
END IF;
v_cnt := v_cnt + 1;
v_receiveid := rec_receive.id;
v_productid := rec_receive.productid;
v_lot_number := rec_receive.lotnumber;
v_lot_quantity := rec_receive.quantitycounted;
v_box_quantity := rec_receive.boxcounted;
v_lotflag := rec_receive.lotflag;
v_expirydate := rec_receive.expirydate;
v_received_quantity := rec_receive.quantity;
v_boxNumber := rec_receive.boxNumber;
v_location_id := rec_receive.locationId;
v_vvm_id := rec_receive.vvmid;
INSERT INTO inspection_lots(vvmstatus,
inspectionlineitemid, lotnumber, countedquantity, createddate, modifieddate,expirydate,receivedQuantity,boxNumber,passlocationId)
VALUES (v_vvm_id,v_inspectonlineitemid, v_lot_number, v_lot_quantity, NOW(), NOW(),v_expirydate,v_received_quantity,v_boxNumber,v_location_id);
v_old_productid := rec_receive.productid;
END LOOP;
v_result:=0;
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
END;$BODY$;

ALTER FUNCTION public.fn_create_inpsection(integer)
    OWNER TO postgres;
