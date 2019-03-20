create table regimen_dispatch_transaction
(
  id              serial       NOT NULL PRIMARY KEY,
  facilityId      INTEGER      NOT NULL references facilities (id),
  regimenId       INTEGER      NOT NULL,
  clientId        VARCHAR(200) NOT NULL,
  transactionDate DATE         NOT NULL,
  days            INTEGER      NOT NULL,
  quantity        INTEGER      NOT NULL,

  createdBy       integer,
  createdDate     TIMESTAMP,
  modifiedBy      integer,
  modifiedDate    TIMESTAMP
)