package com.don.trade.mq;

import javax.management.NotificationEmitter;

public interface OnMessageMBean extends NotificationEmitter {
	public long getTotalCalls();
	public long getTotalNano();
	public void reset();
	public long getLastCallNano();
	public long getLastCallTime();
	public double getAverage();
	
}
