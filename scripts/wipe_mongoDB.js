use StockSim;
db.dropDatabase();
use StockSim;
db.createCollection("users");
db.users.createIndex({ "username": 1, "portfolios.name": 1 });
db.createCollection("admins");
exit