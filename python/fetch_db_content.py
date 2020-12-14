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

# move files in a folder
def move_symbols(symbols, dest):
	for s in symbols:
		filename = '{}.csv'.format(s)
		shutil.move(join('hist', filename), join(dest, filename))

# try to retrive an optional attribute
def try_fetch_attr( source, doc, attname):
    try:
        doc[attname]= source[attname]
    except KeyError:
        pass

# clean up
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
offset = 0	# in case of download failure, no need to start from the beginning
limit = 10000	# number of ticker symbols to be retrieved
period = 'max'	# valid periods: 1d,5d,1mo,3mo,6mo,1y,2y,5y,10y,ytd,max

# retrieve U.S. stock market ticker symbols
data = pd.read_csv("http://www.nasdaqtrader.com/dynamic/SymDir/nasdaqtraded.txt", sep='|')
data_clean = data[data['Test Issue'] == 'N']
symbols = data_clean['Symbol'].tolist()

print('Total number of symbols traded = {}'.format(len(symbols)))

# adjust configs based on retrieved symbols list size
limit = limit if limit else len(symbols)
end = min(offset + limit, len(symbols))
is_valid = [False] * len(symbols)

titlesCollection=[] #collection to be populated
# for each ticker symbol
for i in range(offset, end):
	s = symbols[i]
	Curr_Ticker = yf.Ticker(s)

	# retrieve and save ticker company info
	try:
		print(Curr_Ticker.info.info['symbol'] + " - " + Curr_Ticker.info.info['shortName'])

		# common fileds
		estractedTitle={
  			"currency": Curr_Ticker.info["currency"],
 			"shortName": Curr_Ticker.info["shortName"],
  			"longName": Curr_Ticker.info["longName"],
  			"exchangeTimezoneName": Curr_Ticker.info["exchangeTimezoneName"],
  			"exchangeTimezoneShortName": Curr_Ticker.info["exchangeTimezoneShortName"],
  			"quoteType": Curr_Ticker.info["quoteType"],
  			"ticker": Curr_Ticker.info["symbol"],
  			"market": Curr_Ticker.info["market"],
  			"logoURL": Curr_Ticker.info["logo_url"]
		}
		# optional fields
		try_fetch_attr(Curr_Ticker.info,estractedTitle,"sector")
		try_fetch_attr(Curr_Ticker.info,estractedTitle,"city")
		try_fetch_attr(Curr_Ticker.info,estractedTitle,"website")
		try_fetch_attr(Curr_Ticker.info,estractedTitle,"industry")
		try_fetch_attr(Curr_Ticker.info,estractedTitle,"longBusinessSummary")
		location={} # location is a subdocument
		try_fetch_attr(Curr_Ticker.info,location,"state")
		try_fetch_attr(Curr_Ticker.info,location,"country")
		try_fetch_attr(Curr_Ticker.info,location,"phone")
		# unionize address fields
		try:
			location["address"]= location["address1"]+" "+Curr_Ticker.info["address2"]
		except KeyError:
			try:
				location["address"]= Curr_Ticker.info["address1"]
			except KeyError:
				pass
		estractedTitle["location"]=(location)
		titlesCollection.append(estractedTitle)
	except:
		pass
		continue
	# retrieve ticker histoical data
	Curr_Ticker.info = yf.download(s, period=period)
	
	# in case of errors during historical data retrieval, continue
	if len(Curr_Ticker.info.index) == 0:
		continue

	# all downloads completed without errors: ticker symbol is valid
	is_valid[i] = True
	Curr_Ticker.info.to_csv('hist/{}.csv'.format(s))
	print("\n")

# data retrieval ended
print('Total number of valid symbols downloaded = {}'.format(sum(is_valid)))
print("dumping collection in json/TickerCollection.json")
with open("json/TickerCollection.json", 'w') as fp:
	json.dump(titlesCollection, fp) #dump everything in one file 
# import collection commands
# altert1: mongoimport from mongoTools is required
# aleter2: no merge is performed
#os.system("mongoimport -h=172.16.3.94:27017 --jsonArray --db StokSim --collection stocks --file json/TickerCollection.json").
#os.system("mongoimport  --jsonArray --db StokSim --collection stocks --file json/TickerCollection.json").

# create csv containing successfully retrived symbols stock data
valid_data = data_clean[is_valid]
valid_data.to_csv('symbols_valid_meta.csv', index=False)
etfs = valid_data[valid_data['ETF'] == 'Y']['Symbol'].tolist()
stocks = valid_data[valid_data['ETF'] == 'N']['Symbol'].tolist()

# divide etfs and stocks in different directories
move_symbols(etfs, "etfs")
move_symbols(stocks, "stocks")

