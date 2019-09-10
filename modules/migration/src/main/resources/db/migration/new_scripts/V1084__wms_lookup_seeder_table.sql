
--Delete all data first

DELETE FROM supply_partners;
DELETE FROM ports;
DELETE FROM document_types;


--insert lookup data

INSERT INTO supply_partners(
            code, name, isactive, createdby, createddate, modifiedby,
            modifieddate)
    VALUES ('UNICEF', 'UNICEF', True, 307, NOW(), 307,
            NOW());


INSERT INTO ports(
            code, name, description, active, createdby, createddate,
            modifiedby, modifieddate)
    VALUES ('DSM' , 'Dar es Salaam Port', 'Located in dar', True, 307, NOW(),
            307, NOW());



INSERT INTO public.document_types(
            name, description, createdby, createddate, modifiedby, modifieddate)
    VALUES ('Airway Bill', 'This is awb', 307, NOW(), 307, NOW()),('Packing List', 'This is packinglist', 307, NOW(), 307, NOW()),('Invoice', 'This is invoice', 307, NOW(), 307, NOW()),('Release Certificate', 'This is certs', 307, NOW(), 307, NOW());