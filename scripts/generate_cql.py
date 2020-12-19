#!/usr/bin/env python

##
# Prepares the .csv files in 'hist' to be imported into Apache Cassandra.
# Generates the CQL script to import the .csv files in 'hist' into Apache Cassandra.
##

import os
import glob
import pandas as pd
from os.path import isfile, join

__author__ = "Marco Pinna, Yuri Mazzuoli and Rambod Rahmani"
__copyright__ = "Copyright (C) 2020 Marco Pinna, Rambod Rahmani and Yuri Mazzuoli"
__license__ = "GPLv3"

# prepare .csv files to be imported into apache cassandra
convert_dict = {'symbol': str} 
  
for filename in os.listdir('hist/'):
    print("Converting: " + filename)
    symbol = os.path.splitext(filename)[0]
    df = pd.read_csv("hist/" + filename)
    df.columns = ['symbol', 'date', 'open', 'high', 'low', 'close', 'adj_close', 'volume']
    df = df.astype(convert_dict)

    i = 0
    while i < len(df.index):
        df.at[i, "symbol"] = symbol
        i += 1

    df.to_csv("hist/" + filename, index=False)

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
f.write("CREATE TABLE IF NOT EXISTS tickers (" + "\n")
f.write("	symbol text," + "\n")
f.write("	date date," + "\n")
f.write("	open decimal," + "\n")
f.write("	high decimal," + "\n")
f.write("	low decimal," + "\n")
f.write("	close decimal," + "\n")
f.write("	adj_close decimal," + "\n")
f.write("	volume decimal," + "\n")
f.write("	primary key ((symbol), Date)" + "\n")
f.write(") WITH CLUSTERING ORDER BY (Date DESC);" + "\n")
f.write("\n")

# close .cql file after writing
f.close()

tickers = 0

# for each ticker symbol
for file in os.listdir('./json'):
    if os.path.exists("hist/" + os.path.splitext(file)[0] + ".csv") and os.path.exists("json/" + file):
        s = os.path.splitext(file)[0]

        # open .cql file for wrinting
        f = open("stocksim.cql", "a")
        f.write("COPY tickers FROM '" + s + ".csv' WITH DELIMITER=',' AND HEADER=TRUE;" + "\n")
        f.write("\n")

        # close .cql file after writing
        f.close()

        tickers = tickers + 1
    else:
        print("json/" + file)
        os.remove("json/" + file)

print("Total number of valid tickers = " + str(tickers))
