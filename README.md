# mylab

In this repository you'll find a little executable jar to calculate some index on stock symbol.
The jar contains the source code.
Double click on the executable jar (JRE 7) will open a JPanel to interface the available operations.
First of all you've insert one of the stock symbol included in the sampledata.properties (in the following rows).
Panel messages and systemOut trace make evidence of the results.

a)	For a given stock

    i.	calculate the dividend yield
  
    ii.	calculate the P/E Ratio
  
    iii.	record a trade, with timestamp, quantity of shares, buy or sell indicator and price
  
    iv.	Calculate Stock Price based on trades recorded in past 15 minutes

b)	Calculate the GBCE All Share Index using the geometric mean of prices for all stocks


sampledata.properties (included in the jar):

StockSymbol	Type	LastDividend	FixedDividend	ParValue	
TEA	          Common	    0		                      100	
POP	          Common	    8		                      100	
ALE	          Common	    23		                       60   
JOE	          Common	    13		                     250
GIN	          Preferred	     8	          2%	          100	


The execution of the jar (JRE 7) will produces the following files:
- a file xxx_trade.txt for each stock symbol
- a file AllStockPrice.txt in which collect all the stock price 
 
