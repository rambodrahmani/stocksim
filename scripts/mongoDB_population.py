import os
os.system("mongo < wipe_mongoDB.js")
os.system("mongoimport  --jsonArray --db stocksim --collection stocks --file stocks_coll.json")
#os.system("mongoimport -h=172.16.3.94:27017 --jsonArray --db StokSim --collection stocks --file stocks_coll.json")

# usage:
# run in the same folder of the js script and the colllection file. 
# to get  the collection file:
# - if you have the separated jsons, just run the merge script in the right folder
# - if you don't have json files, just run the fetch script