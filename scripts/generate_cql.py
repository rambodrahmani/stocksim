#!/usr/bin/env python

"""
Generates the CQL script to import the .csv files in 'hist' into Apache Cassandra.
"""

import os
import glob
from os.path import isfile, join

__author__ = "Marco Pinna, Yuri Mazzuoli and Rambod Rahmani"
__copyright__ = "Copyright (C) 2007 Free Software Foundation, Inc."
__license__ = "GPLv3"

# open .cql file for wrinting
f = open("stocksim.cql", "w")

# write to .cql file
f.write("/*" + "\n")
f.write(" * Apache Cassandra Stocksim CQL Query." + "\n")
f.write(" *" + "\n")
f.write(" * author: Marco Picca, Rambod Rahmani, Yuri Mazzuoli." + "\n")
f.write(" */" + "\n")
f.write("DROP KEYSPACE stocksim;" + "\n")
f.write("\n")
f.write("CREATE KEYSPACE IF NOT EXISTS stocksim" + "\n")
f.write("WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};" + "\n")
f.write("\n")
f.write("USE stocksim;" + "\n")
f.write("\n")

# close .cql file after writing
f.close()

TABLES = 0

# for each ticker symbol
for file in os.listdir('./json'):
    if os.path.exists("hist/" + os.path.splitext(file)[0] + ".csv") and os.path.exists("json/" + file):
        s = os.path.splitext(file)[0]

        # open .cql file for wrinting
        f = open("stocksim.cql", "a")

        # write to .cql file
        f.write("CREATE TABLE IF NOT EXISTS " + s + " (" + "\n")
        f.write("	id int," + "\n")
        f.write("	date date," + "\n")
        f.write("	open decimal," + "\n")
        f.write("	high decimal," + "\n")
        f.write("	low decimal," + "\n")
        f.write("	close decimal," + "\n")
        f.write("	adj_close decimal," + "\n")
        f.write("	volume decimal," + "\n")
        f.write("	primary key (id, Date)" + "\n")
        f.write(") WITH CLUSTERING ORDER BY (Date DESC);" + "\n")
        f.write("COPY " + s + " FROM '" + s + ".csv' WITH DELIMITER=',' AND HEADER=TRUE;" + "\n")
        f.write("\n")

        # close .cql file after writing
        f.close()

        TABLES = TABLES + 1
    else:
        print("json/" + file)
        os.remove("json/" + file)

print("TABLES = " + str(TABLES))
