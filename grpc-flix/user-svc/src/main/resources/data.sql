DROP TABLE IF EXISTS ms_user;
CREATE TABLE ms_user AS SELECT * FROM CSVREAD('classpath:data.csv');