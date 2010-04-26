package com.don.trade.data;
import static com.don.trade.data.OrderType.LIMIT_BUY;
import static com.don.trade.data.OrderType.LIMIT_SELL;
import static com.don.trade.data.OrderType.MARKET_BUY;
import static com.don.trade.data.OrderType.MARKET_SELL;
import static com.don.trade.data.OrderType.STOP_BUY;
import static com.don.trade.data.OrderType.STOP_SELL;

import java.security.InvalidParameterException;

public class OrderEntry {
	private boolean active;
	private double price;
	private long quantity;
	private StringBuffer symbol;
	private OrderType type;
	
	private OrderEntry() {	}
	
	public OrderEntry(StringBuffer s, double p, long q, OrderType t) {
		if( q <= 0 ) 
			throw new InvalidParameterException();
		
		price = p;
		quantity = q;
		symbol = new StringBuffer(s);
		active  = true;
		type = t;
	}
	
	
	public double getPrice() {
		return price;
	}
	
	public long getQuantity() {
		return quantity;
	}
	
	public StringBuffer getSymbol() {
		return symbol;
	}
	
	public OrderType getType() {
		return type;
	}
	
	public boolean isLimitOrder() {
		return (type == LIMIT_BUY || type == LIMIT_SELL);
	}
	
	public boolean isStopOrder() {
		return (type == STOP_BUY || type == STOP_SELL);
	}
	
	public boolean isMarketOrder() {
		return (type == MARKET_BUY || type == MARKET_SELL);
	}
	
	public boolean isSellOrder() {
		return (type == LIMIT_SELL || type == STOP_SELL || type == MARKET_SELL);
	}
	
	public boolean isBuyOrder() {
		return (type == LIMIT_BUY || type == STOP_BUY || type == MARKET_BUY);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean comesBefore (double price) {
		if( this.isBuyOrder())
			return (this.price < price);
		else 
			return (this.price > price);
	}
}
