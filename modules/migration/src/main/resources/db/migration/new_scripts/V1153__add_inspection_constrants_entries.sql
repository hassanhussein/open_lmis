ALTER TABLE inspection_lots
ADD CONSTRAINT inspection_lots_Unique UNIQUE (inspectionLineItemId,lotNumber,passLocationId,vvmStatus);