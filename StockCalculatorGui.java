package com.my.stock;

import javax.swing.JFrame;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class StockCalculatorGui extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel messageLabelPeriodToCalStockPrice;
	private JLabel messageLabelPeriodToRecordTrade;
	private JLabel messageLabelTickerPrice;
	private JLabel messageLabelStockName;
	private JTextField tickerPrice;
	private JTextField stockNameTextField;
	private JTextField periodToRecordTradeTextField;
	private JTextField periodToCalcStockPrice;
	private JButton calcStockPriceButton;
	private JButton calcDivYeldButton;
	private JButton recordTradeButton;
	private JButton gBCEButton;
	private final int WINDOW_WIDTH = 700;
	private final int WINDOW_HEIGHT = 200;

	public StockCalculatorGui()
	{
		setTitle("Retail Price Calculator");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildPanel();
		add(panel);
		setVisible(true);
	}

	private void buildPanel()
	{

		messageLabelStockName = new JLabel("                               Enter here the Stock symbol for which to calculate all the following indexes and values:");
		stockNameTextField = new JTextField(3);
		messageLabelTickerPrice = new JLabel("Enter ticker Price:");
		tickerPrice = new JTextField(10);
		calcDivYeldButton = new JButton ("Calculate Dividend Yeld and P/E Ratio");
		calcDivYeldButton.addActionListener(new CalcButtonListenerDiv());
		messageLabelPeriodToRecordTrade = new JLabel("Period express in minutes to record trade ");
		periodToRecordTradeTextField=new JTextField(3);
		new JLabel("Stock for which generates trade");
		recordTradeButton = new JButton ("Record trade");
		recordTradeButton.addActionListener(new recordTradeButtonListener());
		new JLabel("Stock for which calculate the price");
		messageLabelPeriodToCalStockPrice=new JLabel("Backward time range for which calculate the stock price (minutes)");
		periodToCalcStockPrice=new JTextField(3);
		calcStockPriceButton = new JButton ("Calculate Stock Price");
		calcStockPriceButton.addActionListener(new CalcStockPriceListener());
		gBCEButton=new JButton("Calculate GBCE for all the stocks");
		gBCEButton.addActionListener(new GBCEListener());
		
		panel = new JPanel();
	
		panel.add(messageLabelStockName);
		panel.add(stockNameTextField);
		panel.add(messageLabelTickerPrice);
		panel.add(tickerPrice);
		panel.add(calcDivYeldButton);
		panel.add(messageLabelPeriodToRecordTrade);
		panel.add(periodToRecordTradeTextField);
		panel.add(recordTradeButton);
		panel.add(messageLabelPeriodToCalStockPrice);
		panel.add(periodToCalcStockPrice);
		panel.add(calcStockPriceButton);
		panel.add(gBCEButton);
	}

	
	
	
	//Listener to calculate GBCE for all the stocks
		private class GBCEListener implements ActionListener
		{

			public void actionPerformed(ActionEvent e)
			{
				
				String fileStockPrice = "AllStockPrice.txt";
				
				int nStocks=0;
				double productsStocks=1;
				double gBCE=0;
				
				try {
			         
					
					File file = new File(fileStockPrice);
			         Scanner scanner = new Scanner(file);
			         while (scanner.hasNextLine()) {
							String[] items = scanner.nextLine().split(",");
							String stprice = items[1];
							double wholestkprice = Double.parseDouble(stprice);
							productsStocks=productsStocks*wholestkprice;
							System.out.println("ProductsStocks ="+productsStocks);
							nStocks=nStocks+1;
												
					}
			            
			         	gBCE= Math.pow(productsStocks, (double)nStocks);
						JOptionPane.showMessageDialog(null, "All Share Index GBCE for "+nStocks+" stocks "+"is "+gBCE);
						System.out.println("All Share Index GBCE for "+nStocks+" stocks "+"is "+gBCE);
  					    scanner.close();
			       } catch (IOException ex) {
			         ex.printStackTrace();
			       }
				
				
			}
		}
	
	
	
	
	//Listener to calculate price of each stock
	private class CalcStockPriceListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			
			String fileInName = stockNameTextField.getText()+"_trade.txt";
			boolean runCalculator = true;
	
			double wholeTradePrice=0;
			double wholeTradeQuantity=0;
			double wholeStockPrice=0;
			double wholeProduct=0;
			
			Calendar endTimeToCalcStockPrice = Calendar.getInstance();
	        java.util.Date endTime = endTimeToCalcStockPrice.getTime();
	        java.sql.Timestamp endTimestamp = new java.sql.Timestamp(endTime.getTime());
	        java.sql.Timestamp startTimestamp= new Timestamp(endTimestamp.getTime()- new Integer(periodToCalcStockPrice.getText())*60*1000); 
	       
			try {
		         File file = new File(fileInName);
		         Scanner scanner = new Scanner(file);
		         while (runCalculator&&scanner.hasNextLine()) {
						String[] items = scanner.nextLine().split(",");
						String timeRecord = items[0];
						Timestamp timeRecordData = Timestamp.valueOf(timeRecord);
						
						String quantity = items[1];
						double wholeQuantity = Double.parseDouble(quantity);
						
						String price = items[3];
						double wholePrice= Double.parseDouble(price);
						
						if (timeRecordData.after(startTimestamp)&&(timeRecordData.before(endTimestamp))){
							wholeTradePrice=wholeTradePrice+wholePrice;	
							wholeTradeQuantity=wholeTradeQuantity+wholeQuantity;
							wholeProduct=wholeProduct+(wholeTradePrice*wholeQuantity);
							System.out.println("Numerator "+wholeProduct);
							System.out.println("Dividend "+wholeTradeQuantity);
						} else {
							runCalculator=false;
						} 
				}
		            wholeStockPrice=wholeProduct/wholeTradeQuantity;
		            System.out.println("Stock price value: "+wholeStockPrice);
		            JOptionPane.showMessageDialog(null, "End of elaboration, stock price value:  "+wholeStockPrice);
					
					FileWriter wr = new FileWriter("AllStockPrice.txt", true);
					PrintWriter outFile = new PrintWriter(wr);
					if (Double.isNaN(wholeStockPrice)) {
						System.out.println("Stock price value not valid");
					} else {
						outFile.println(stockNameTextField.getText()+","+wholeStockPrice);
					} 
					
      			    outFile.close();
					scanner.close();
		       } catch (IOException ex) {
		         ex.printStackTrace();
		       }
			
			
		}
	}

	//Listener to calculate dividend
	private class CalcButtonListenerDiv implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			String lastDivData;
			String tickerPriceInput;
			String stockDetails;
			String fixedDivData;
			String parValueData;
			String stockName;
			double wholeLastDiv;
			double wholeTickerPrice=0;
			double wholefixedDiv=0;
			double wholeParValue=0;
			double valuePERatio=0;
			double dividendYield=0;
			
			
			Properties prop = new Properties();
	    	InputStream input=null;
	    	
	    	try {
	        
	    		input = getClass().getClassLoader().getResourceAsStream("sampledata.properties");
	    		if(input==null){
	    	            System.out.println("Sorry, unable to find " + "sampledata.properties");
	    		    return;
	    		}
	    		
	    		
	    		//load a properties file from class path, inside static method
	    		prop.load(input);
	    		stockName=stockNameTextField.getText();
	    		
	    		if (prop.getProperty(stockName)==null) {
	    			System.out.println("Stock option " + stockName +" is not available");
	    			JOptionPane.showMessageDialog(null,"Stock  " + stockName + "  is not present in our sampple data");   
	    			return;
				} else {
					stockDetails= prop.getProperty(stockName);

					try {
						tickerPriceInput = tickerPrice.getText();
						wholeTickerPrice = Double.parseDouble(tickerPriceInput);
					} catch (Exception e2) {
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, "Value data is empty, please fill in"); 
						return;
					}
					
					String[] items = stockDetails.split(",");
						for (String item : items) {
								System.out.println("item = " + item);
								}
						if (items[0]=="Preferred") {
							
							lastDivData = items[1];
							wholeLastDiv = Double.parseDouble(lastDivData);
							fixedDivData = items[2];
							wholefixedDiv = Double.parseDouble(fixedDivData);
							parValueData = items[3];
							wholeParValue = Double.parseDouble(parValueData);
							dividendYield = (( wholefixedDiv * wholeParValue) / wholeTickerPrice);
														
						} else {
							
							lastDivData = items[1];
							wholeLastDiv = Double.parseDouble(lastDivData);
							dividendYield = (wholeLastDiv / wholeTickerPrice);

						} 
						valuePERatio = (wholeTickerPrice/wholeLastDiv);
						
						System.out.println("Stock details "+ stockName + "  " +stockDetails);
						System.out.println("Last Divindend for stock "+ stockName + " : £" + dividendYield);
						System.out.println("P/E Ratio value for stock "+ stockName + " : " + valuePERatio);
					
				}
	 
	    	JOptionPane.showMessageDialog(null, "Stock "+ stockName + "-  dividend yield :  £" + dividendYield + "--  P/E Ratio : "+valuePERatio);   
	 
	    	} catch (IOException ex) {
	    		System.out.println("File properties not found");
	    		ex.printStackTrace();
	    		JOptionPane.showMessageDialog(null, "IO exception, please check the data input");   
	        } finally{
	        	if(input!=null){
	        		try {
					input.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Generic Exception, please check the data input");   
				}
	        	}
	        }
			
			
		}
	}
	
	
	//Listener to record trades
	private class recordTradeButtonListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{

			
			String periodToRecordTrade;
			periodToRecordTrade=periodToRecordTradeTextField.getText();

            Calendar calStart = Calendar.getInstance();
	        java.util.Date startTime = calStart.getTime();
	        java.sql.Timestamp startTimestamp = new java.sql.Timestamp(startTime.getTime());
	        java.sql.Timestamp endTimestamp= new Timestamp(startTimestamp.getTime()+ new Integer(periodToRecordTrade)*60*1000); 
	        java.sql.Timestamp currentTimestamp;
	        boolean noEndRecordLoop = true;
	        int unitShares;
	 
	        //IndicatorBuySale=> Buy==True   Sale==False
	        boolean indicatorBuySale;
	        String indicator;
	        String priceUnit=tickerPrice.getText();
			double wholePriceUnit=0; 
			Random random = new Random();
			
			String stockTrade=stockNameTextField.getText();
			
			
			try
		     {
		          FileOutputStream outStream = new FileOutputStream(stockTrade+"_trade.txt");
		          PrintStream writeStream = new PrintStream(outStream);
		          
		         while (noEndRecordLoop) {
					
				         	 
		        	  indicator="S";
		        	  Calendar cal = Calendar.getInstance();
		        	  java.util.Date now = cal.getTime();
		        	  currentTimestamp = new java.sql.Timestamp(now.getTime());
		        	   	  
		        	  
		        	  unitShares = random.nextInt(1000000);
		        	  indicatorBuySale = random.nextBoolean();
		        	  if (indicatorBuySale) {
		        		  			indicator="B";						
					  }
		        	  wholePriceUnit = Double.parseDouble(priceUnit);
		        	  wholePriceUnit=wholePriceUnit*random.nextDouble();
		        	  writeStream.println( currentTimestamp+"," +unitShares+","+ indicator +","+wholePriceUnit);
		        	  
		        	  if (currentTimestamp.after(endTimestamp)) {
		        		  noEndRecordLoop=false;
		        		  writeStream.close();
							
						}
		        	 Thread.sleep(1000);
		          }
		          
		      }
		      catch (Exception eOut)
		      {
		          System.out.println("Error: " + eOut);
		          System.exit(1);
		      }
			System.out.println("End of recording trade");
			JOptionPane.showMessageDialog(null, "Trade recorded");
		}
	}
	
	public static void main(String[] args)
	{
		new StockCalculatorGui();
	}
}
