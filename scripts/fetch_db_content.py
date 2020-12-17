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

# copy the given files from source to destination
def copy_symbols(source, dest):
    for s in source:
        filename = '{}.csv'.format(s)
        shutil.copy(join('hist', filename), join(dest, filename))

refresh_start = input("Would you like to make a fresh start? [y/n] ")
if (refresh_start == "y"):
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

# for each ticker symbol
for i in range(offset, end):
    s = symbols[i]

    if not os.path.exists("json/" + s + ".json") or os.path.exists("csv/" + s + ".csv"):
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

        # retrieve ticker histoical data
        data = yf.download(s, period = period)

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
    else:
        is_valid[i] = True
        print("json/" + s + ".json" + " EXISTS.\n")

# data retrieval ended
print('Total number of valid symbols downloaded = {}'.format(sum(is_valid)))

# create csv containing successfully retrived symbols stock data
valid_data = data_clean[is_valid]
valid_data.to_csv('symbols_valid_meta.csv', index=False)
etfs = valid_data[valid_data['ETF'] == 'Y']['Symbol'].tolist()
stocks = valid_data[valid_data['ETF'] == 'N']['Symbol'].tolist()

# copy etfs and stocks in different directories
copy_symbols(etfs, "etfs")
copy_symbols(stocks, "stocks")
