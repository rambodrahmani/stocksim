import json
import os

# merge ticker in different document into 1 json copmposed by an array of document

__author__ = "Marco Pinna, Yuri Mazzuoli and Rambod Rahmani"
__copyright__ = "Copyright (C) 2007 Free Software Foundation, Inc."
__license__ = "GPLv3"

# fetch optional attributes
def try_fetch_attr( source, dest, attname):
    try:
        dest[attname]= source[attname]
    except KeyError:
        pass
# open JSONs directory
files=os.listdir("json/")
ticker_collection=[] # collection to be filled
for json_file in files:

    # retrieve and save ticker company info
    ticket_object=json.load(open(os.path.join("json/",json_file)))

    # common fields
    estractedFields={
        "currency": ticket_object["currency"],
        "shortName": ticket_object["shortName"],
        "longName": ticket_object["longName"],
        "exchangeTimezoneName": ticket_object["exchangeTimezoneName"],
        "exchangeTimezoneShortName": ticket_object["exchangeTimezoneShortName"],
        "quoteType": ticket_object["quoteType"],
        "ticker": ticket_object["symbol"],
        "market": ticket_object["market"],
        "logoURL": ticket_object["logo_url"]
    }
     # optional fields
    try_fetch_attr(ticket_object,estractedFields,"sector")
    try_fetch_attr(ticket_object,estractedFields,"city")
    try_fetch_attr(ticket_object,estractedFields,"website")
    try_fetch_attr(ticket_object,estractedFields,"industry")
    try_fetch_attr(ticket_object,estractedFields,"longBusinessSummary")
    location={} # location is a subdocument
    try_fetch_attr(ticket_object,location,"state")
    try_fetch_attr(ticket_object,location,"country")
    try_fetch_attr(ticket_object,location,"phone")
     # union addres fields
    try:
        location["address"]= location["address1"]+" "+ticket_object["address2"]
    except KeyError:
        try:
            location["address"]= ticket_object["address1"]
        except KeyError:
            pass
    estractedFields["location"]=(location)
    ticker_collection.append(estractedFields)  # append new document
# write collection in the new file
with open("stocks_coll.json", "w") as target_file:
   json.dump(ticker_collection,target_file)

#os.system("mongoimport -h=172.16.3.94:27017 --jsonArray --db StokSim --collection stocks --file stocks_coll.json").
#os.system("mongoimport  --jsonArray --db StokSim --collection stocks --file stocks_coll.json").



