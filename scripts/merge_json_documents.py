import json
import os

##
# Merge tickers .json documents into one single .json document as an array of documents.
##

__author__ = "Marco Pinna, Rambod Rahmani and Yuri Mazzuoli"
__copyright__ = "Copyright (C) 2020 Marco Pinna, Rambod Rahmani and Yuri Mazzuoli"
__license__ = "GPLv3"

# fetch optional attributes
def try_fetch_attr( source, dest, attname):
    try:
        dest[attname] = source[attname]
    except KeyError:
        pass

# open JSONs directory
files = os.listdir("json/")

# collection to be filled
ticker_collection = []

for json_file in files:
    # retrieve and save ticker company info
    ticket_object = json.load(open(os.path.join("json/", json_file)))

    # common fields
    estractedFields = {
        "currency": ticket_object["currency"],
        "shortName": ticket_object["shortName"],
        "longName": ticket_object["longName"],
        "exchangeTimezoneName": ticket_object["exchangeTimezoneName"],
        "exchangeTimezoneShortName": ticket_object["exchangeTimezoneShortName"],
        "quoteType": ticket_object["quoteType"],
        "symbol": ticket_object["symbol"],
        "market": ticket_object["market"],
        "logoURL": ticket_object["logo_url"]
    }

    # optional fields: might or might not be present in the original json file
    try_fetch_attr(ticket_object, estractedFields, "marketCap")
    try_fetch_attr(ticket_object, estractedFields, "trailingPE")
    try_fetch_attr(ticket_object, estractedFields, "sector")
    try_fetch_attr(ticket_object, estractedFields, "website")
    try_fetch_attr(ticket_object, estractedFields, "industry")
    try_fetch_attr(ticket_object, estractedFields, "longBusinessSummary")

    # location enbedded document
    location={}
    try_fetch_attr(ticket_object, location, "city")
    try_fetch_attr(ticket_object, location, "state")
    try_fetch_attr(ticket_object, location, "country")
    try_fetch_attr(ticket_object, location, "phone")

    # compose address field
    try:
        location["address"] = location["address1"] + " " + ticket_object["address2"]
    except KeyError:
        try:
            location["address"] = ticket_object["address1"]
        except KeyError:
            pass
    estractedFields["location"] = (location)

    # append new document for the current ticker
    ticker_collection.append(estractedFields)

# write collection in the new file
with open("stocks.json", "w") as target_file:
   json.dump(ticker_collection, target_file)

#os.system("mongoimport -h=172.16.3.94:27017 --jsonArray --db StokSim --collection stocks --file stocks_coll.json").
#os.system("mongoimport  --jsonArray --db StokSim --collection stocks --file stocks_coll.json").
