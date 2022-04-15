CREATE SCHEMA IF NOT EXISTS reports;


CREATE TABLE reports.document
(
    id  SERIAL PRIMARY KEY,
    amount bigint,
    name    varchar(150) NOT NULL CONSTRAINT document_name_unique UNIQUE ,
    number bigint,
    content varchar(500),
    status varchar(200),
    customer varchar(200),
    supplier varchar(200),
    created timestamp with time zone,
    modified timestamp with time zone
);
