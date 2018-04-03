CREATE TABLE cold_chain_equipment_temperature_alarms (
  id                   SERIAL PRIMARY KEY,
  equipmentInventoryId INT           NOT NULL REFERENCES equipment_inventories (id),

  alarmId              VARCHAR(50)   NOT NULL,
  alarmDate            DATE          NOT NULL,

  startTime            DATE          NULL,
  endTime              DATE          NULL,
  alarmType            VARCHAR(500)  NULL,
  status               VARCHAR(5000) NULL,

  -- Audit fields
  createdDate          TIMESTAMP     NOT NULL DEFAULT NOW(),
  createdBy            INT,
  modifiedDate         TIMESTAMP,
  modifiedBy           INT
);

