Drop TABLE IF EXISTS support_bot_subscribers;
CREATE TABLE support_bot_subscribers (
    id SERIAL PRIMARY KEY,
    rnrId INTEGER NOT NULL REFERENCES requisitions(id),
    chatId varchar(100) NOT NULL,
    label varchar(100) NOT NULL,
    source varchar(100) NOT NULL,
    active boolean NOT NULL,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modifiedBy INTEGER,
    modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);