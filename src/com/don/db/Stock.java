package com.don.db;

public class Stock {
	String symbol;
	String date;
	double open;
	double high;
	double low;
	double close;
	long vol;
	double ev;
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public long getVol() {
		return vol;
	}
	public void setVol(long vol) {
		this.vol = vol;
	}
	public double getEv() {
		return ev;
	}
	public void setEv(double ev) {
		this.ev = ev;
	}
	
	
}
