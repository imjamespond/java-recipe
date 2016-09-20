CREATE SEQUENCE user_id_seq start 10001 increment by 1;

DROP TABLE IF EXISTS fly_users;
CREATE TABLE fly_users (
id BIGINT NOT NULL ,
username VARCHAR( 128 ) NOT NULL ,
email VARCHAR( 256 ) NOT NULL ,
rose INT NOT NULL ,
germ INT NOT NULL ,
gold INT NOT NULL ,
score INT NOT NULL ,
gender INT NOT NULL ,
contribute INT NOT NULL ,
groupName VARCHAR( 128 ) NOT NULL ,
relationship bytea NOT NULL ,
item bytea NOT NULL ,
fashion bytea NOT NULL ,
PRIMARY KEY ( id ) 
);

alter table fly_users add column gift bytea not null default '';
alter table fly_users add column charm integer not null default 0;
alter table fly_users add column nickname VARCHAR( 128 ) not null default '';
alter table fly_users add column logindate BIGINT not null default 0;
alter table fly_users add column guide BIGINT not null default 0;

DROP TABLE IF EXISTS fly_rank;
CREATE TABLE fly_rank (
id BIGINT NOT NULL ,
contribute integer not null default 0,
charm integer not null default 0,
weeknum integer not null default 0,
PRIMARY KEY ( id ) 
);