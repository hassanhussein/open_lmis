ALTER TABLE inspection_lots DROP CONSTRAINT IF EXISTS inspection_lots_Unique;

ALTER TABLE inspection_lots
ADD CONSTRAINT inspection_lots_Unique UNIQUE (inspectionLineItemId,lotNumber,passLocationId,vvmStatus);