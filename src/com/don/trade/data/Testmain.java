package com.don.trade.data;
import static java.lang.System.out;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Testmain {
	public static void main( String [] args) throws InterruptedException {
		OrderType ot = OrderType.LIMIT_BUY;
		out.println(ot.getAsString(ot));
		out.println(ot.toString());
		
		int concurrency = 3;
		final CountDownLatch ready = new CountDownLatch(concurrency);
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch done = new CountDownLatch(concurrency);
		
		final Runnable action = new Runnable() {
			@Override
			public void run() {
				System.out.println("runnnnn");
			}
			
		};
		ExecutorService execService = Executors.newFixedThreadPool(concurrency);
		for( int i=0; i<concurrency; i++) {
			execService.execute(new Runnable() {
				public void run() {
					ready.countDown();
					
					try {
						start.await();
						action.run();
					} catch( InterruptedException e) {
						Thread.currentThread().interrupt();
					} finally {
						done.countDown();
					}
				}
			});
		}
		
		ready.await();
		long startNanos = System.nanoTime();
		start.countDown();
		done.await();
		System.out.println(System.nanoTime() - startNanos);
	}
	
	public static void test(OrderType o){
		
	}
}
