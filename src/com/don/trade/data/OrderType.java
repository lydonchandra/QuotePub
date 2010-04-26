package com.don.trade.data;

import java.util.HashMap;
import java.util.Map;

public enum OrderType {
	LIMIT_BUY("Limit Buy Order") { void apply(){ } } ,
	LIMIT_SELL("Limit Sell Order") { void apply(){ } } ,
	STOP_BUY("Stop Buy Order") { void apply(){ } } ,
	STOP_SELL("Stop Sell Order") { void apply(){ } } ,
	MARKET_BUY("Market Buy Order") { void apply(){ } } ,
	MARKET_SELL("Market Sell Order") { void apply(){ } } ;
	
	private final String orderString;
	
	OrderType(String orderString) {
		this.orderString = orderString;
	}
	
	public String getAsString(OrderType o) {
		return this.orderString;
	}
	
	@Override 
	public String toString() {
		return this.orderString;
	}
	
	abstract void apply(); 
	
	private static final Map<String, OrderType> stringToEnum = new HashMap<String, OrderType>();
	
	static {
		for( OrderType ot : values())
			stringToEnum.put(ot.toString(), ot);
	}
	
	public static OrderType fromString(String symbol) {
		return stringToEnum.get(symbol);
	}
	
//	public String getAsString(OrderType o) {
//		switch(o) {
//		case LIMIT_BUY:
//			return "Limit Buy Order";
//		case LIMIT_SELL:
//			return "Limit Sell Order";
//		case STOP_BUY:
//			return "Stop Buy Order";
//		case STOP_SELL:
//			return "Stop Sell Order";
//		case MARKET_BUY:
//			return "Market Buy Order";
//		case MARKET_SELL:
//			return "Market Sell Order";
//		}
//		return "Invalid Order Type";
//	}
}
