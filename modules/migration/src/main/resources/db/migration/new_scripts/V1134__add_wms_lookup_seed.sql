DELETE FROM PORTS WHERE CODE = 'JNIA';
INSERT INTO ports(
            code, name, description, active, createdby, createddate,
            modifiedby, modifieddate)
    VALUES ('JNIA' , 'JNIA', 'Located in dar', True, 307, NOW(),
            307, NOW());