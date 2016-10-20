# Users schema

# --- !Ups

CREATE TABLE User (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    fullname varchar(255) NOT NULL,
    isAdmin boolean NOT NULL,
    PRIMARY KEY (id)
);


DROP TABLE IF EXISTS t9a;
CREATE TABLE t9a (
  term varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  ename varchar(255) DEFAULT NULL,
  sex varchar(255) DEFAULT NULL,
  party varchar(255) DEFAULT NULL,
  partygroup varchar(255) DEFAULT NULL,
  areaname varchar(255) DEFAULT NULL,
  district varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  committee varchar(255) DEFAULT NULL,
  onboarddate varchar(255) DEFAULT NULL,
  degree text,
  profession text,
  experience text,
  alltel varchar(255) DEFAULT NULL,
  labtel varchar(255) DEFAULT NULL,
  servicetel1 varchar(255) DEFAULT NULL,
  servicetel2 varchar(255) DEFAULT NULL,
  servicetel3 varchar(255) DEFAULT NULL,
  servicetel4 varchar(255) DEFAULT NULL,
  servicetel5 varchar(255) DEFAULT NULL,
  labfax varchar(255) DEFAULT NULL,
  servicefax1 varchar(255) DEFAULT NULL,
  servicefax2 varchar(255) DEFAULT NULL,
  servicefax3 varchar(255) DEFAULT NULL,
  servicefax4 varchar(255) DEFAULT NULL,
  servicefax5 varchar(255) DEFAULT NULL,
  picurl varchar(255) DEFAULT NULL,
  leavedate varchar(255) DEFAULT NULL,
  alladdr varchar(255) DEFAULT NULL,
  labaddr varchar(255) DEFAULT NULL,
  serviceaddr1 varchar(255) DEFAULT NULL,
  serviceaddr2 varchar(255) DEFAULT NULL,
  serviceaddr3 varchar(255) DEFAULT NULL,
  serviceaddr4 varchar(255) DEFAULT NULL,
  serviceaddr5 varchar(255) DEFAULT NULL,
  facebook varchar(255) DEFAULT NULL,
  wiki varchar(255) DEFAULT NULL,
  lineid varchar(255) DEFAULT NULL
) ;
insert into t9a select * from csvread('data/t9a.h2.csv');

DROP TABLE IF EXISTS district2name;
create table district2name (name varchar(16),amount int, district varchar(1000));
insert into district2name select * from csvread('data/district2name.h2.csv');

DROP TABLE IF EXISTS profession2name;
create table profession2name(name varchar(16),amount int, profession varchar(1000));
insert into profession2name select * from csvread('data/profession2name.h2.csv');

DROP TABLE IF EXISTS t9asearch;
create table t9asearch (name varchar(255), content varchar(4000));
insert into t9asearch select * from csvread('data/t9asearch.h2.csv');


# --- !Downs

DROP TABLE User;