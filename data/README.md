**調整過的資料內容方便放進系統裡使用**


#t9a
``` sql
drop table if exists t9a;
create table t9a (
term varchar(255),
name varchar(255),
ename varchar(255),
sex varchar(255),
party varchar(255),
partygroup varchar(255),
areaname varchar(255),
district varchar(255),
email varchar(255),
committee varchar(255),

onboarddate varchar(255),
degree text,
profession text,
experience text,
alltel varchar(255),
labtel varchar(255),
servicetel1 varchar(255),
servicetel2 varchar(255),
servicetel3 varchar(255),
servicetel4 varchar(255),

servicetel5 varchar(255),
labfax varchar(255),
servicefax1 varchar(255),
servicefax2 varchar(255),
servicefax3 varchar(255),
servicefax4 varchar(255),
servicefax5 varchar(255),
picurl varchar(255),
leavedate varchar(255),
alladdr varchar(255),

labaddr varchar(255),
serviceaddr1 varchar(255),
serviceaddr2 varchar(255),
serviceaddr3 varchar(255),
serviceaddr4 varchar(255),
serviceaddr5 varchar(255),
facebook varchar(255),
wiki varchar(255),
lineid varchar(255)
);
```

### 9a.csv前處理
```
replace \n asdfasdf
replace asdfasdf9 \n9
replace asdfasdf \r
```
### import, export
load data local infile 'c:\\9a.csv' into table t9a fields terminated by ',' lines terminated by '\n' ignore 1 lines ;
select *,replace(degree,'\r','/') ,replace(experience,'\r','/')  from t9a limit 5,1\G
select * into outfile 't9a.mysq.csv'  FIELDS TERMINATED BY ',' ENCLOSED BY '"' ESCAPED BY '\\' LINES TERMINATED BY '\n' FROM  t9a;

