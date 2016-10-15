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
replace asdfasfd離職 \n離職
replace asdfasdf \r
replace \r /
#csv檔內換行可能是\r\n，會造成匯入database後最後欄的解讀困難，必要時要使用編輯器強制換行符為\n
#"Missouri ,MBA"裡有','符號造成問題時需移除
```

原始資料是用msysql做格式轉換的，但最終系統內用的是經量的in-memory database h2。
**h2 database比較沒有mysql對csv容錯強度高，t9ah2.csv是最終處理出h2可以接受的格式，也是系統起動時h2主動去取得匯入資料表的內容。(參考/conf/evolutions/default/1.sql)**

### import, export
truncate t9a;
load data local infile 'c:\\9a.csv' into table t9a fields terminated by ',' lines terminated by '\n' ignore 1 lines ;
update t9a set lineid='' where lineid is null;
select *,replace(degree,'\r','/') ,replace(experience,'\r','/')  from t9a limit 5,1\G
select count(*) from t9a;
select distinct term from t9a;
select distinct lineid from t9a;
select wiki,lineid  from t9a ;
select * into outfile 't9a.mysq.csv'  FIELDS TERMINATED BY ',' ENCLOSED BY '"' ESCAPED BY '\\' LINES TERMINATED BY '\n' FROM  t9a;
\#select * into outfile 't9a.mysq.csv'  FIELDS TERMINATED BY ',' ESCAPED BY '\\' LINES TERMINATED BY '\n' FROM  t9a;
"c:\program files\mysql\mysql server 5.6\bin\mysqldump"  --extended-insert=FALSE  --verbose --user=root --password=asdfasdf;lkj drifty t9a

