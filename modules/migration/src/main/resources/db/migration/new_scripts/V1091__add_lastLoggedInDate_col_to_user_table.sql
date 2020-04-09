ALTER TABLE users DROP COLUMN IF EXISTS lastLoggedInDate;
ALTER TABLE users  ADD COLUMN lastLoggedInDate TIMESTAMP;
CREATE INDEX ON users (lastLoggedInDate);