/*
 * Apache Cassandra Stocksim CQL Query.
 *
 * author: Marco Picca, Rambod Rahmani, Yuri Mazzuoli.
 */
DROP KEYSPACE stocksim;

CREATE KEYSPACE IF NOT EXISTS stocksim
WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

USE stocksim;

CREATE TABLE ZS (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZS FROM 'ZS.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZSAN (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZSAN FROM 'ZSAN.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZSL (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZSL FROM 'ZSL.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZTO (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZTO FROM 'ZTO.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZTR (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZTR FROM 'ZTR.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZTS (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZTS FROM 'ZTS.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZUMZ (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZUMZ FROM 'ZUMZ.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZUO (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZUO FROM 'ZUO.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZVO (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZVO FROM 'ZVO.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZYME (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZYME FROM 'ZYME.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZYNE (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZYNE FROM 'ZYNE.csv' WITH DELIMITER=',' AND HEADER=TRUE;

CREATE TABLE ZYXI (
	id int,
	date date,
	open decimal,
	high decimal,
	low decimal,
	close decimal,
	adj_close decimal,
	volume decimal,
	primary key (id, Date)
) WITH CLUSTERING ORDER BY (Date DESC);
COPY ZYXI FROM 'ZYXI.csv' WITH DELIMITER=',' AND HEADER=TRUE;

