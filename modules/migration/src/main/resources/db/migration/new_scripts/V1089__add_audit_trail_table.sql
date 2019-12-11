DROP TABLE IF EXISTS audit_trails;

CREATE TABLE audit_trails (
  id serial PRIMARY KEY,
  action character varying(100) NOT NULL,
  userfullname character varying(100),
  userid int NOT NULL REFERENCES users(id),
  identity character varying(100),
  identityvalue character varying(100),
  oldvalue character varying(100),
  newvalue character varying(100),
  createddate timestamp without time zone
)