ALTER TABLE lot_on_hand_locations
    DROP COLUMN IF EXISTS fromBinLocationId;
ALTER TABLE lot_on_hand_locations
    ADD COLUMN fromBinLocationId Integer;