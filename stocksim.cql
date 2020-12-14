/*
 * Apache Cassandra Stocksim CQL Query.
 *
 * author: Marco Picca, Rambod Rahmani, Yuri Mazzuoli.
 */
DROP KEYSPACE stocksim;

CREATE KEYSPACE IF NOT EXISTS stocksim
WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

USE stocksim;

CREATE TABLE A (
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
