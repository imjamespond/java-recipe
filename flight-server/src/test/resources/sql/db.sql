CREATE SEQUENCE user_id_seq start 10001 increment by 1;

DROP TABLE IF EXISTS fly_users;
CREATE TABLE fly_users (
id BIGINT NOT NULL ,
username VARCHAR( 128 ) NOT NULL ,
email VARCHAR( 256 ) NOT NULL ,
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


#alter table fly_users add column mscore integer not null default 0;
#alter table fly_users drop column gold;

#CREATE INDEX username ON fly_users USING hash (username);
REINDEX index username;
CREATE INDEX fly_users_charm ON fly_users USING btree (charm);
CREATE INDEX fly_users_contribute ON fly_users USING btree (contribute);

CREATE SEQUENCE fly_rank_seq start 1 increment by 1;
DROP TABLE IF EXISTS fly_rank;
CREATE TABLE fly_rank (
id BIGINT NOT NULL ,
uid BIGINT not null default 0,
contribute integer not null default 0,
charm integer not null default 0,
weeknum integer not null default 0,
PRIMARY KEY ( id ) 
);
alter table fly_rank add column datenum integer not null default 0;
alter table fly_rank add column prizeid integer not null default 0;
alter table fly_rank add column yearnum integer not null default 0;
alter table fly_rank add column monthnum integer not null default 0;
alter table fly_rank add column apple_in integer not null default 0;
alter table fly_rank add column apple_out integer not null default 0;
alter table fly_rank add column match integer not null default 0;
alter table fly_rank add column elapsed bigint not null default 0;

alter table fly_rank drop column prizeid;
alter table fly_rank drop column apple_in;
alter table fly_rank drop column apple_out;

CREATE INDEX weeknum ON fly_rank USING btree (weeknum);
CREATE INDEX monthnum ON fly_rank USING btree (monthnum);
CREATE INDEX yearnum ON fly_rank USING btree (yearnum);
CREATE INDEX charm ON fly_rank USING btree (charm);
CREATE INDEX apple_out ON fly_rank USING btree (apple_out);
CREATE INDEX apple_in ON fly_rank USING btree (apple_in);



CREATE TABLE applerecord
(
  id bigint NOT NULL,
  fromuid bigint,
  nickname character varying(256),
  touid bigint,
  number integer,
  time bigint,
  CONSTRAINT applerecord_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE applerecord
  OWNER TO postgres;

CREATE SEQUENCE applerecord_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE applerecord_id_seq
  OWNER TO postgres;
  
  
insert into fly_wealth (id,rose, gems,apple,credit)
select id,rose, gems,apple,credit from fly_users;
CREATE SEQUENCE fly_wealth_seq start 10001 increment by 1;
DROP TABLE IF EXISTS fly_wealth;
CREATE TABLE fly_wealth (
id BIGINT NOT NULL ,
rose INT NOT NULL ,
gems INT NOT NULL ,
apple integer not null default 0,
credit integer not null default 0,
PRIMARY KEY ( id ) 
);

insert into fly_data (id, score, contribute,charm,group_,guide,item,fashion,gift,relationship,upgrade)
select id, score, contribute,charm,group_,guide,item,fashion,gift,relationship,'' as upgrade from fly_users;
CREATE SEQUENCE user_data_seq start 10001 increment by 1;
DROP TABLE IF EXISTS fly_data;
CREATE TABLE fly_data (
id BIGINT NOT NULL ,
guide BIGINT not null default 0,
score INT NOT NULL ,
contribute INT NOT NULL ,
charm integer not null ,
group_ VARCHAR( 128 ) NOT NULL ,
relationship bytea NOT NULL ,
item bytea NOT NULL ,
fashion bytea NOT NULL ,
gift bytea not null,
upgrade bytea not null,
PRIMARY KEY ( id ) 
);

alter table fly_users drop column rose;
alter table fly_users drop column gems;
alter table fly_users drop column apple;
alter table fly_users drop column credit;
alter table fly_users drop column guide;
alter table fly_users drop column score;
alter table fly_users drop column contribute;
alter table fly_users drop column charm;
alter table fly_users drop column group_;
alter table fly_users drop column relationship;
alter table fly_users drop column item;
alter table fly_users drop column fashion;
alter table fly_users drop column gift;

alter table fly_data add column pve integer not null default 0;
alter table fly_data add column pvetime BIGINT not null default 0;
alter table fly_data add column matchnum integer not null default 0;
alter table fly_data add column matchepoch BIGINT not null default 0;
alter table fly_data add column gold integer not null default 0;
alter table fly_data add column matchrec bytea not null default '';

truncate table fly_users;
truncate table fly_rank;
truncate table fly_data;
truncate table fly_wealth;