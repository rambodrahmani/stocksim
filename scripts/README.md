# Prerequisites
Install required python modules
```bash
pip3 install --user pandas yfinance
```

# Usage
Simply run
```bash
python3 fetch_db_content.py
```
This will create the following directories:
* ```hist```: .csv files with tickers historical data;
* ```json```: .json files with tickers company details;

Tickers are also devided into the following sub directories:
* ```stocks```
* ```etfs```

You can now run
```bash
python3 generate_cql.py
```
which will produce ```stocksim.cql``` to be used to populate Apache Cassandra.

A single .json document can be obtained using
```bash
python3 merge_json_documents.py
```
which will produce a single file ```stocks_coll.json``` to be used to populate MongoDB.
