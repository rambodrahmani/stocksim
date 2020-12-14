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

def move_symbols(symbols, dest):
    for s in symbols:
        filename = '{}.csv'.format(s)
        shutil.move(join('hist', filename), join(dest, filename))

# fetch optional attributes
def try_fetch_attr( source, dest, attname):
    try:
        dest[attname]= source[attname]
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
offset = 0		# in case of download failure, no need to start from the beginning
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

# preparing file for the collection
with open("json/stocks_coll.json", "w") as target_file:
    target_file.write("[")
# for each ticker symbol
for i in range(offset, end):
    s = symbols[i]
    Curr_Ticker = yf.Ticker(s)

    # retrieve and save ticker company info
    try:
        print(Curr_Ticker.info['symbol'] + " - " + Curr_Ticker.info['shortName'])
        ticker_object=Curr_Ticker.info
        # common fields
        estractedFields={
            "currency": ticker_object["currency"],
            "shortName": ticker_object["shortName"],
            "longName": ticker_object["longName"],
            "exchangeTimezoneName": ticker_object["exchangeTimezoneName"],
            "exchangeTimezoneShortName": ticker_object["exchangeTimezoneShortName"],
            "quoteType": ticker_object["quoteType"],
            "ticker": ticker_object["symbol"],
            "market": ticker_object["market"],
            "logoURL": ticker_object["logo_url"]
        }
        # optional fields
        try_fetch_attr(ticker_object,estractedFields,"sector")
        try_fetch_attr(ticker_object,estractedFields,"city")
        try_fetch_attr(ticker_object,estractedFields,"website")
        try_fetch_attr(ticker_object,estractedFields,"industry")
        try_fetch_attr(ticker_object,estractedFields,"longBusinessSummary")
        location={} # location is a subdocument
        try_fetch_attr(ticker_object,location,"state")
        try_fetch_attr(ticker_object,location,"country")
        try_fetch_attr(ticker_object,location,"phone")
        # union addres fields
        try:
            location["address"]= location["address1"]+" "+ticker_object["address2"]
        except KeyError:
            try:
                location["address"]= ticker_object["address1"]
            except KeyError:
                pass
        estractedFields["location"]=(location)
        # append new document
        with open("json/stocks_coll.json", "a") as target_file:
            json.dump(estractedFields,target_file)
            if i!=(end-1):
                target_file.write(", ")
            else:
                target_file.write("]") # close the collection
    except:
        pass
        continue

    # retrieve ticker histoical data
    data = yf.download(s, period=period)
    
    # rename column names
    data.reset_index(inplace=True)
    data.columns = ["date", "open", "high", "low", "close", "adj_close", "volume"]
    
    # insert id column
    data.insert(0, "id", range(0, len(data.index)), True)
    
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
