CREATE SCHEMA IF NOT EXISTS reports;


CREATE TABLE reports.document
(
    id  SERIAL PRIMARY KEY,
    name    varchar(150) NOT NULL CONSTRAINT document_name_unique UNIQUE ,
    type varchar(10),
    content varchar(500),
    status varchar(200),
    customer varchar(200),
    supplier varchar(200),
    created timestamp with time zone,
    modified timestamp with time zone
);

CREATE TABLE reports.template
(
    id  SERIAL PRIMARY KEY,
    name varchar(200) NOT NULL,
    link varchar(500) NOT NULL,
    type varchar(10) NOT NULL
)
