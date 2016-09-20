CREATE SEQUENCE web_users_id_seq start 10001 increment by 1;

DROP TABLE IF EXISTS web_users;
CREATE TABLE web_users (
id BIGINT NOT NULL ,
username VARCHAR( 128 ) NOT NULL ,
password VARCHAR( 256 ) NOT NULL ,
salt VARCHAR( 256 ) default '',
email VARCHAR( 256 ) default '',
phone VARCHAR( 256 ) default '',
name VARCHAR( 256 ) default '',
identity VARCHAR( 256 ) default '',
registertime BIGINT not null default 0,
appletime BIGINT not null default 0,
activetime BIGINT not null default 0,
state INT NOT NULL ,
accountstate INT NOT NULL ,
appleState INT NOT NULL ,
PRIMARY KEY ( id ) 
);

CREATE SEQUENCE game_id_seq increment by 1;
DROP TABLE IF EXISTS game;
CREATE TABLE game (
id BIGINT NOT NULL,
gname VARCHAR( 256 ) ,
type INT NOT NULL ,
state INT NOT NULL ,
PRIMARY KEY ( id )
);

CREATE SEQUENCE pay_id_seq START 101;
CREATE SEQUENCE logger_id_seq START 101;

DROP TABLE IF EXISTS web_payment;
CREATE TABLE web_payment (
id BIGINT NOT NULL,
uid BIGINT NOT NULL,
payment double precision NOT NULL default 0,
type integer not null default 0,
state integer not null default 0,
order_id VARCHAR( 256 ) UNIQUE ,
description VARCHAR( 256 ) not null default '',
create_time bigint not null default 0,
finish_time bigint not null default 0,
PRIMARY KEY ( id )
);

DROP TABLE IF EXISTS rose;
DROP TABLE IF EXISTS web_logger;
CREATE TABLE web_logger (
id BIGINT NOT NULL,
uid BIGINT NOT NULL,
logdate BIGINT NOT NULL,
num integer not null default 0,
current integer not null default 0,
type integer not null default 0,
discription VARCHAR( 256 ) not null default '',
PRIMARY KEY ( id )
);


CREATE SEQUENCE game_vote_id_seq start 10001 increment by 1;
DROP TABLE IF EXISTS game_vote;
CREATE TABLE game_vote (
gid bigint NOT NULL ,
trend_1 integer NOT NULL default 1,
trend_2 integer NOT NULL default 1,
trend_3 integer NOT NULL default 1,
trend_4 integer NOT NULL default 1,
trend_5 integer NOT NULL default 1,
trend_6 integer NOT NULL default 1,
trend_7 integer NOT NULL default 1,
trend_8 integer NOT NULL default 1,
trend_9 integer NOT NULL default 1,
trend_0 integer NOT NULL default 1,
vote_1 integer NOT NULL default 80,
vote_2 integer NOT NULL default 80,
vote_3 integer NOT NULL default 80,
vote_4 integer NOT NULL default 80,
vote_5 integer NOT NULL default 80,
vote_6 integer NOT NULL default 80,
vote_7 integer NOT NULL default 80,
vote_8 integer NOT NULL default 80,
vote_9 integer NOT NULL default 80,
vote_0 integer NOT NULL default 80,
last_1 integer NOT NULL default 0,
last_2 integer NOT NULL default 0,
last_3 integer NOT NULL default 0,
last_4 integer NOT NULL default 0,
last_5 integer NOT NULL default 0,
last_6 integer NOT NULL default 0,
last_7 integer NOT NULL default 0,
last_8 integer NOT NULL default 0,
last_9 integer NOT NULL default 0,
last_0 integer NOT NULL default 0,
PRIMARY KEY ( gid ) 
);
  
CREATE SEQUENCE web_vote_id_seq start 10001 increment by 1;
DROP TABLE IF EXISTS web_vote;
CREATE TABLE web_vote (
uid bigint NOT NULL ,
vote character varying(256) NOT NULL,
epochweek integer NOT NULL ,
PRIMARY KEY ( uid ) 
);

CREATE SEQUENCE web_match_id_seq start 10001 increment by 1;
DROP TABLE IF EXISTS web_match;
CREATE TABLE web_match (
id bigint NOT NULL ,
uid bigint NOT NULL ,
mid integer NOT NULL ,
rank integer NOT NULL ,
time bigint NOT NULL ,
PRIMARY KEY ( id ) 
);

alter table comment add column tid bigint not null default 0;

#兑换记录
CREATE SEQUENCE web_exchange_id_seq start 10001 increment by 1;
DROP TABLE IF EXISTS web_exchange;
CREATE TABLE web_exchange (
id bigint NOT NULL ,
uid bigint NOT NULL ,
time_ bigint NOT NULL ,
type_ integer NOT NULL ,
item integer NOT NULL ,
num integer NOT NULL ,
invoice character varying(256) NOT NULL,
remark character varying(512) NOT NULL,
PRIMARY KEY ( id ) 
);
CREATE INDEX web_exchange_uid ON web_exchange USING btree (uid);
REINDEX index web_exchange_uid;

#联系方式
CREATE SEQUENCE web_contact_id_seq start 10001 increment by 1;
DROP TABLE IF EXISTS web_contact;
CREATE TABLE web_contact (
uid bigint NOT NULL ,
name character varying(256) NOT NULL,
mobile character varying(256) NOT NULL,
email character varying(256) NOT NULL,
address character varying(512) NOT NULL,
postcode character varying(256) NOT NULL,
PRIMARY KEY ( uid ) 
);

#登录记录
CREATE SEQUENCE web_signin_id_seq start 10001 increment by 1;
DROP TABLE IF EXISTS web_signin;
CREATE TABLE web_signin (
id bigint NOT NULL ,
uid bigint NOT NULL ,
date_ bigint NOT NULL ,
ip character varying(64) NOT NULL,
PRIMARY KEY ( id ) 
);
CREATE INDEX web_signin_uid ON web_signin USING btree (uid);
