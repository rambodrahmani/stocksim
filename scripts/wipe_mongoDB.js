use stocksim;
db.dropDatabase();
use stocksim;
db.createCollection("users");
db.users.createIndex({ "username": 1 }, {unique: true});
db.createCollection("admins");
db.createCollection("stocks");
db.stocks.createIndex({ "ticker": 1 }, {unique: true});
db.stocks.createIndex({ "trailingPE": 1 });
exit