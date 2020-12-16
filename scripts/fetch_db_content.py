#!/usr/bin/env python

"""
Retrieves historical data and company information for all the ticker symbols
of the U.S. Stock Market.
"""

import os
import json
import shutil
import contextlib
import pandas as pd
import yfinance as yf
from os.path import isfile, join

__author__ = "Marco Pinna, Yuri Mazzuoli and Rambod Rahmani"
__copyright__ = "Copyright (C) 2007 Free Software Foundation, Inc."
__license__ = "GPLv3"

# moves the given files from source to destination
def move_symbols(source, dest):
	for s in source:
		filename = '{}.csv'.format(s)
		shutil.move(join('hist', filename), join(dest, filename))

# clean up
os.remove('stocksim.cql')

if os.path.exists('etfs'):
	shutil.rmtree('etfs')
os.mkdir('etfs')

if os.path.exists('hist'):
	shutil.rmtree('hist')
os.mkdir('hist')

if os.path.exists('json'):
	shutil.rmtree('json')
os.mkdir('json')

if os.path.exists('stocks'):
	shutil.rmtree('stocks')
os.mkdir('stocks')

# configs
offset = 9430	# in case of download failure, no need to start from the beginning
limit = 10000	# number of ticker symbols to be retrieved
period = 'max'	# valid periods: 1d,5d,1mo,3mo,6mo,1y,2y,5y,10y,ytd,max

# retrieve U.S. stock market ticker symbols
data = pd.read_csv("http://www.nasdaqtrader.com/dynamic/SymDir/nasdaqtraded.txt", sep='|')
data_clean = data[data['Test Issue'] == 'N']
symbols = data_clean['Symbol'].tolist()

print('Total number of symbols traded = {}'.format(len(symbols)))

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

# adjust configs based on retrieved symbols list size
limit = limit if limit else len(symbols)
end = min(offset + limit, len(symbols))
is_valid = [False] * len(symbols)

# for each ticker symbol
for i in range(offset, end):
	s = symbols[i]
	Curr_Ticker = yf.Ticker(s)

	# retrieve and save ticker company info
	try:
		print(Curr_Ticker.info['symbol'] + " - " + Curr_Ticker.info['shortName'])
		fp = open("json/" + s + ".json", "w")
		json.dump(Curr_Ticker.info, fp)
		fp.close()
	except:
		pass
		continue

	# open .cql file for wrinting
	f = open("stocksim.cql", "a")

	# write to .cql file
	f.write("CREATE TABLE " + s + " (" + "\n")
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

	# retrieve ticker histoical data
	data = yf.download(s, period = period, threads = True)

	# rename column names
	data.reset_index(inplace=True)
	data.columns = ["date", "open", "high", "low", "close", "adj_close", "volume"]

	# insert id column
	data.insert(0, "id", [i] * len(data.index), True)
	
	# in case of errors during historical data retrieval, continue
	if len(data.index) == 0:
		continue

	# all downloads completed without errors: ticker symbol is valid
	is_valid[i] = True
	data.to_csv('hist/{}.csv'.format(s), index = False)
	print("\n")

# data retrieval ended
print('Total number of valid symbols downloaded = {}'.format(sum(is_valid)))

# create csv containing successfully retrived symbols stock data
valid_data = data_clean[is_valid]
valid_data.to_csv('symbols_valid_meta.csv', index=False)
etfs = valid_data[valid_data['ETF'] == 'Y']['Symbol'].tolist()
stocks = valid_data[valid_data['ETF'] == 'N']['Symbol'].tolist()

# divide etfs and stocks in different directories
move_symbols(etfs, "etfs")
move_symbols(stocks, "stocks")
