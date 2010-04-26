package com.don.trade.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.don.trade.data.OrderEntry;
import com.don.trade.data.OrderType;
import com.don.trade.mq.OnMessage;
import com.don.trade.mq.TradeManagement;
import com.don.trade.util.ISO8601DateFormat;

public class OrderManager implements Runnable {

	private static final String TRADES_TAG = "<trades>";
	private static final String TRADES_CLOSE = "</trades>";
	private static final String TRADE_TAG = "<trade>";
	private static final String TRADE_CLOSE = "</trade>";
	private static final String TYPE_TAG = "<type>";
	private static final String TYPE_CLOSE = "</type>";
	private static final String SYMBOL_TAG = "<symbol>";
	private static final String SYMBOL_CLOSE = "</symbol>";
	private static final String DATETIME_TAG = "<datetime>";
	private static final String DATETIME_CLOSE = "</datetime>";
	private static final String TRADEPRICE_TAG = "<tradeprice>";
	private static final String TRADEPRICE_CLOSE = "</tradeprice>";
	private static final String LIMITPRICE_TAG = "<limitprice>";
	private static final String LIMITPRICE_CLOSE = "</limitprice>";
	private static final String STOPPRICE_TAG = "<stopprice>";
	private static final String STOPPRICE_CLOSE = "</stopprice>";
	private static final String VOLUME_TAG = "<volume>";
	private static final String VOLUME_CLOSE = "</volume>";
	private static final String BUY = "Buy";
	private static final String SELL = "Sell";
	private static final ISO8601DateFormat isoDf = new ISO8601DateFormat();
	
	private static final int MAX_TRADE_XML_SIZE = 512;
	private StringBuffer tradeXML = new StringBuffer(MAX_TRADE_XML_SIZE);
	
	private MarketManager marketMgr = null;
	private TradeManagement tradeMgr = null;
	private OnMessage onMessage = new OnMessage("OrderManager");
	
	public static final int INITIAL_CAPACITY = 111;
	class SubBook {
		LinkedList<OrderEntry> buy = new LinkedList<OrderEntry>();
		LinkedList<OrderEntry> sell = new LinkedList<OrderEntry>();
	}
	
	Map<StringBuffer, SubBook> orderBook = new HashMap<StringBuffer, SubBook>(INITIAL_CAPACITY);
	
	StringBuffer[] orderBookKeys = null;
	
	public OrderManager(MarketManager marketMgr) throws Exception {
		this.marketMgr = marketMgr;
		this.tradeMgr = new TradeManagement("LimitStopTrades");
		
		try {
			File file = new File("orders.csv");
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			
			String line = reader.readLine();
			while( line != null ) {
				int separator = line.indexOf(',');
				String symbol = line.substring(0, separator);
				int begin = separator;
				separator = line.indexOf(',', begin+1);
				String price = line.substring(begin+1, separator);
				begin = separator;
				separator = line.indexOf(',', begin+1);
				String qty = line.substring(begin+1, separator);
				begin = separator;
				separator = line.indexOf(',', begin+1);
				String type = line.substring(begin+1);
				OrderType orderType = OrderType.LIMIT_BUY;
				if( type.equalsIgnoreCase("limitbuy"))
					orderType = OrderType.LIMIT_BUY;
				else if( type.equalsIgnoreCase("limitsell"))
					orderType = OrderType.LIMIT_SELL;
				else if( type.equalsIgnoreCase("stopbuy")) 
					orderType = OrderType.STOP_BUY;
				else if( type.equalsIgnoreCase("stopsell"))
					orderType = OrderType.STOP_SELL;
				
				line = reader.readLine();
				enterOrder( Double.parseDouble(price), Integer.parseInt(qty),
							new StringBuffer(symbol), orderType);
				
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void enterOrder( double price, int quantity, StringBuffer symbol, OrderType type) 
				throws Exception {
		OrderEntry newEntry = new OrderEntry(symbol, price, quantity, type);
		
		SubBook sb = orderBook.get(symbol);
		
		if( sb == null ) {
			sb = new SubBook();
			orderBook.put(symbol, sb);
		}
		
		LinkedList<OrderEntry> orders = null;
		if( newEntry.isBuyOrder() )
			orders = sb.buy;
		else 
			orders = sb.sell;
		
		for( int i = 0; i < orders.size(); i++ ) {
			OrderEntry entry = orders.get(i);
			if( newEntry.comesBefore(entry.getPrice())) {
				orders.add( i, newEntry);
				return;
			}
		}
		orders.addLast(newEntry);
	}
	
	public void displayOrderBook() {
		if( orderBookKeys == null ) {
			Set<StringBuffer> keys = orderBook.keySet();
			orderBookKeys = new StringBuffer[orderBook.size()];
			keys.toArray(orderBookKeys);
		}
		
		for( int i = 0; i < orderBook.size(); i++) {
			displayOrderBook(orderBookKeys[i]);
		}
	}
	
	public void displayOrderBook( StringBuffer symbol ) {
		SubBook sb = orderBook.get(symbol);
		if( sb != null ) {
			System.out.println("****************************");
			System.out.println("Buy orders for " + symbol);
			displaySubBook(sb.buy);
			System.out.println("Sell orders for " + symbol);
			displaySubBook(sb.sell);
			System.out.println(" ");
		}
	}
	
	private StringBuffer[] getOrderBookKeys() {
		if( orderBookKeys == null ) {
			Set<StringBuffer> keys = orderBook.keySet();
			keys.toArray(orderBookKeys);
		}
		return orderBookKeys;
	}
	
	public void displaySubBook(LinkedList<OrderEntry> orders) {
		for( int i = 0; i < orders.size(); i++ ) {
			OrderEntry entry = orders.get(i);
			System.out.println( "Price: " + entry.getPrice() + 
								", Qty: " + entry.getQuantity() +
								", Type:" + entry.getType() ) ;
		}
	}
	
	public boolean checkForTrade( OrderEntry entry, double marketPrice ) {
		if( !entry.isActive() )
			return false;
		
		switch( entry.getType() ) {
		case STOP_SELL:
			if( marketPrice <= entry.getPrice() ) {
				generateTradeXML( entry, marketPrice );
				entry.setActive(false);
				return true;
			}
			break;
			
		case LIMIT_SELL:
			if( marketPrice == entry.getPrice()) {
				generateTradeXML(entry, marketPrice);
				entry.setActive(false);
				return true;
			} else if( marketPrice < entry.getPrice() ) {
				System.out.println(
						"*** MISSED LIMIT SELL: Order price: " + entry.getPrice() + 
						", Market price=" + marketPrice + 
						", Stock: " + entry.getSymbol() );
				
			}
			break;
			
		case STOP_BUY:
			if( marketPrice >= entry.getPrice() ) {
				generateTradeXML(entry, marketPrice);
				entry.setActive(false);
				return true;
			}
			break;
		
		case LIMIT_BUY:
			if( marketPrice == entry.getPrice() ) {
				generateTradeXML(entry, marketPrice);
				entry.setActive(false);
				return true;
			} else if( marketPrice > entry.getPrice()) {
				System.out.println(
						"*** MISSED LIMIT BUY: Order price: " + entry.getPrice() +
						", Market price=" + marketPrice + 
						", Stock: " + entry.getSymbol());
			}
			break;
		}
		return false;
	}
	
	private StringBuffer generateTradeXML( OrderEntry entry, double tradePrice ) {
		try {
			tradeXML.append( TRADES_TAG);
			tradeXML.append( TRADE_TAG);
			
			tradeXML.append(TYPE_TAG);
			if( entry.isBuyOrder())
				tradeXML.append(BUY);
			else 
				tradeXML.append(SELL);
			
			tradeXML.append(SYMBOL_TAG);
			tradeXML.append(entry.getSymbol());
			tradeXML.append(SYMBOL_CLOSE);
			
			tradeXML.append(DATETIME_TAG);
			tradeXML.append(ISO8601DateFormat.formatISO(new Date()));
			tradeXML.append(DATETIME_CLOSE);
			
			tradeXML.append(TRADEPRICE_TAG);
			tradeXML.append(tradePrice);
			tradeXML.append(TRADE_CLOSE);
			
			tradeXML.append(LIMITPRICE_TAG);
			tradeXML.append(entry.getPrice());
			tradeXML.append(LIMITPRICE_CLOSE);
			
			tradeXML.append(VOLUME_TAG);
			tradeXML.append(entry.getQuantity());
			tradeXML.append(VOLUME_CLOSE);
			
			tradeMgr.notifyTrade( tradeXML.toString());
			
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return tradeXML;
	}
	public void run() {
		try { 
			long startTime = onMessage.startCall();
			while( true ) {
				getOrderBookKeys();
				for( int i = 0; i < orderBookKeys.length; i++ ) {
					StringBuffer symbol = orderBookKeys[i];
					StringBuffer sPrice = marketMgr.marketBook.get(symbol);
					if( sPrice == null || sPrice.length() == 0) 
						continue;
					
					double marketPrice = Double.parseDouble(sPrice.toString());
					
					SubBook sb = orderBook.get(symbol);
					
					for( int x = 0; x < sb.sell.size(); x++) {
						OrderEntry entry = sb.sell.get(x);
						if( checkForTrade( entry, marketPrice) == true )
							break;
					}
					
					for( int x = 0; x < sb.buy.size(); x++) {
						OrderEntry entry = sb.buy.get(x);
						if( checkForTrade( entry, marketPrice) == true )
							break;
					}
				}
				onMessage.endCall( startTime);
				startTime = onMessage.startCall();
				//RealtimeThread.waitForNextPeriod();
				Thread.sleep(1);
			}
		} catch (Exception e ) {
			
		}
	}
	
	
	
}
