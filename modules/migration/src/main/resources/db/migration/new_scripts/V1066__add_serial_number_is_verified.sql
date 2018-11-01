ALTER TABLE equipment_inventories
    ADD isSerialNumberVerified BOOLEAN NOT NULL DEFAULT FALSE;

CREATE UNIQUE INDEX unique_validated_serial_numbers
  ON equipment_inventories (serialNumber) WHERE (isSerialNumberVerified = true);