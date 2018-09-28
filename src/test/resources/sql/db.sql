CREATE SEQUENCE user_id_seq start 10001 increment by 1;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
id BIGINT NOT NULL ,
username VARCHAR( 128 ) NOT NULL ,
passwd VARCHAR( 256 ) NOT NULL ,
email VARCHAR( 256 ) NOT NULL ,
imei VARCHAR( 256 ) NOT NULL ,
nickname VARCHAR( 128 ) not null default '',
logindate BIGINT not null default 0,
avatar VARCHAR( 64 ) NOT NULL default '',
gender INT NOT NULL ,
consecutive integer not null default 0,
totaldays integer not null default 0,
totaltime BIGINT not null default 0,
createdate BIGINT not null default 0,
PRIMARY KEY ( id ) 
);

#CREATE INDEX username ON fly_users USING hash (username);
REINDEX index username;
CREATE INDEX fly_users_charm ON fly_users USING btree (charm);
CREATE INDEX fly_users_contribute ON fly_users USING btree (contribute);

  
CREATE SEQUENCE wealth_id_seq start 10001 increment by 1;
DROP TABLE IF EXISTS user_wealth;
CREATE TABLE user_wealth (
id BIGINT NOT NULL ,
gold INT NOT NULL ,
gems INT NOT NULL ,
credit integer not null default 0,
PRIMARY KEY ( id ) 
);

CREATE SEQUENCE data_id_seq start 10001 increment by 1;
DROP TABLE IF EXISTS user_data;
CREATE TABLE user_data (
id BIGINT NOT NULL ,
guide BIGINT not null default 0,
score INT NOT NULL ,
level INT NOT NULL ,
redeem INT NOT NULL ,
relationship bytea NOT NULL ,
item bytea NOT NULL ,
fashion bytea NOT NULL ,
gift bytea not null,
upgrade bytea not null,
PRIMARY KEY ( id ) 
);

truncate table fly_users;
truncate table fly_rank;
truncate table fly_data;
truncate table fly_wealth;