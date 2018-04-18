drop table if exists cold_chain_equipment_temperature_alarms;

CREATE TABLE cold_chain_equipment_temperature_alarms (
  id                   SERIAL PRIMARY KEY,
  equipmentInventoryId INT          NOT NULL REFERENCES equipment_inventories (id),

  alarmId              VARCHAR(50)  NOT NULL,

  startTime            TIMESTAMP    NULL,
  endTime              TIMESTAMP    NULL,
  alarmType            VARCHAR(500) NULL,
  status               VARCHAR(500) NULL,

  -- Audit fields
  createdDate          TIMESTAMP    NOT NULL DEFAULT NOW(),
  createdBy            INT,
  modifiedDate         TIMESTAMP,
  modifiedBy           INT
);

