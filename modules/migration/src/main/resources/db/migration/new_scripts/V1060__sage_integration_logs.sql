create table integration_item_stock_log(
    id serial primary key,
    itemCode varchar(200) not null,
    itemDescription varchar(500) null,
    locationCode varchar(50) not null,
    quantity INT NOT NULL,
    dateUpdated DATE,
    createdDate timestamp default now(),
    createdBy INT,
    modifiedDate timestamp default now(),
    modifiedBy INT
);

create table integration_item_price_log(
    id serial primary key,
    itemCode varchar(200) not null,
    itemDescription varchar(500) null,
    price decimal not null,
    dateUpdated DATE,
    createdDate timestamp default now(),
    createdBy INT,
    modifiedDate timestamp default now(),
    modifiedBy INT
);

