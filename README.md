# StockSim: Stock Portfolio Simulator
StockSim is a Java application which, as main feature, allows users to simulate 
stock market portfolios. The StockSim application is composed by two main 
programs:
* StockSim Server: supposed to be running 24/7 to ensure historical data is 
always up-to-date;
* StockSim Client: can be launched in either ```admin``` or ```user``` mode.

The StockSim Server is not thought to be distributed to end users, whereas the 
StockSim Client can be used by both administrators and normal users. The choice 
was made to provide the same program to both administrators and normal users 
with two different running modes. Administrators can add new ticker symbols, 
new administrator accounts, delete both administrator and normal user accounts. 
Normal users have access to stocks and ETFs historical data, day by day, 
starting from 2010. They can create their own stock portfolios, run simulations 
a visualize the resulting statistics.
