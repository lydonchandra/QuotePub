package com.don.trade.engine;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

public class Main {

//	public Main() {
//		try {
////			java.lang.Class.forName("com.don.trade.engine.MarketManager", true, 
////									this.getClass().getClassLoader()
////			);
////			
////			java.lang.Class.forName("com.don.trade.engine.OrderManager", true,
////									this.getClass().getClassLoader()
////			);
//			
//			final BeanFactory factory = new XmlBeanFactory(new FileSystemResource("/Users/lydonchandra/Documents/workspace-sts-carbon/QuotePub/src/com/don/donjms.xml"));
//			
//			MarketManager marketMgr = new MarketManager();
//			
////			PriorityParameters sched = new PriorityParameters(PriorityScheduler.instance(). 
////											getMaxPriority());
////			PeriodicParameters period = new PeriodicParameters(new RelativeTime(1,0));
////			RealtimeThread orderThread = new RealTimeThread(sched, period, null, null, null, orderMgr);
//			
//			Thread marketThread = new Thread(marketMgr);
//			marketThread.start();
//			OrderManager orderMgr = new OrderManager(marketMgr);
//			orderMgr.displayOrderBook();
//			Thread orderThread = new Thread(orderMgr);
//			
//			//Thread.sleep(200);
//			orderThread.start();
////			while( true ) {
////				System.gc();
////				Thread.sleep(100);
////			}
//			
//		} catch ( Exception e ) {
//			e.printStackTrace();
//		}
//	
//	}
	
	public static void main(String[] args) {
		Main main = new Main();
		
		try {
//			java.lang.Class.forName("com.don.trade.engine.MarketManager", true, 
//									this.getClass().getClassLoader()
//			);
//			
//			java.lang.Class.forName("com.don.trade.engine.OrderManager", true,
//									this.getClass().getClassLoader()
//			);
			
			final BeanFactory factory = new XmlBeanFactory(new FileSystemResource("/Users/lydonchandra/Documents/workspace-sts-carbon/QuotePub/src/com/don/donjms.xml"));
			
			
//			PriorityParameters sched = new PriorityParameters(PriorityScheduler.instance(). 
//											getMaxPriority());
//			PeriodicParameters period = new PeriodicParameters(new RelativeTime(1,0));
//			RealtimeThread orderThread = new RealTimeThread(sched, period, null, null, null, orderMgr);

			MarketManager marketMgr = new MarketManager();
			OrderManager orderMgr = new OrderManager(marketMgr);
			orderMgr.displayOrderBook();

			Thread marketThread = new Thread(marketMgr);
			Thread orderThread = new Thread(orderMgr);
			marketThread.start();
			orderThread.start();
//			while( true ) {
//				System.gc();
//				Thread.sleep(100);
//			}
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
	}

}
