DROP TABLE IF EXISTS ms_movie;
CREATE TABLE ms_movie AS SELECT * FROM CSVREAD('classpath:data.csv');