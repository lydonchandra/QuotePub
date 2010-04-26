package com.don.trade.engine;

public class Main {

	public Main() {
		try {
			java.lang.Class.forName("com.don.trade.engine.MarketManager", true, 
									this.getClass().getClassLoader()
			);
			
			java.lang.Class.forName("com.don.trade.engine.OrderManager", true,
									this.getClass().getClassLoader()
			);
			
			MarketManager marketMgr = new MarketManager();
			OrderManager orderMgr = new OrderManager(marketMgr);
			
//			PriorityParameters sched = new PriorityParameters(PriorityScheduler.instance(). 
//											getMaxPriority());
//			PeriodicParameters period = new PeriodicParameters(new RelativeTime(1,0));
//			RealtimeThread orderThread = new RealTimeThread(sched, period, null, null, null, orderMgr);
			orderMgr.displayOrderBook();
			Thread marketThread = new Thread(marketMgr);
			Thread orderThread = new Thread(orderMgr);
			marketThread.start();
			orderThread.start();
			while( true ) {
				System.gc();
				Thread.sleep(100);
			}
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Main main = new Main();
	}

}
