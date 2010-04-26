package com.don.trade.data;

public class MarketPrice {
	private double price;
	private long quantity;
	private StringBuffer timestamp;
	private StringBuffer symbol;
	
	private MarketPrice() {}
	public MarketPrice(StringBuffer s, double p, long q, StringBuffer t) {
		price = p;
		quantity = q;
		timestamp = new StringBuffer(t);
		symbol = new StringBuffer(s);
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price){
		this.price = price;
	}
	public long getQuantity(){
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public StringBuffer getTimestamp() {
		return timestamp;
	}
	public StringBuffer getSymbol() {
		return symbol;
	}
}
