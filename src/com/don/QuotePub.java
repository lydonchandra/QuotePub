package com.don;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;


public class QuotePub {
	public static void main( String [] args) throws JMSException, InterruptedException {
		final BeanFactory factory = new XmlBeanFactory(new FileSystemResource("/Users/lydonchandra/Documents/workspace-sts-carbon/QuotePub/src/com/don/donjms.xml"));
		
		ExecutorService exec = Executors.newFixedThreadPool(2);
		
//		exec.execute(new Runnable() {
//			@Override
//			public void run() {
//				DefaultMessageListenerContainer ml = (DefaultMessageListenerContainer)factory.getBean("jmsContainer");
//				ml.start();
//			}
//		});
		
//		TimeUnit.SECONDS.sleep(2);
		
		exec.execute(new Runnable() {
			@Override
			public void run() {
				Publisher publisher = (Publisher)factory.getBean("publisher");
//				File file = new File("/Users/lydonchandra/Downloads/QuotePublisher/datafeed.csv");
				File file = new File("/Users/lydonchandra/java/stock/datafeeddon.csv");
				FileReader fr;
				try {
					fr = new FileReader(file);
					BufferedReader reader = new BufferedReader(fr);
					
//					String line = reader.readLine();
//		            while ( line != null )
//		            {
//		                int seperator = line.indexOf(',');
//		                String symbol = line.substring(0, seperator);
//		                int begin = seperator;
//		                seperator = line.indexOf(',', begin+1);
//		                String price = line.substring(begin+1, seperator);
//
//		                // Publish this update
//		                publisher.publishQuote(symbol, price);
//		                Thread.sleep(10);
//
//		                // Read the next line of fake update data
//		                line = reader.readLine();
					StringBuilder stringBuilder = new StringBuilder(reader.readLine());
					
					while( stringBuilder.length() > 0 ) {
						int separator = stringBuilder.indexOf(",");
						String symbol = stringBuilder.substring(0, separator);
						int begin = separator;
						separator = stringBuilder.indexOf(",", begin+1);
						String price = stringBuilder.substring(begin+1, separator);
						publisher.publishQuote(symbol, price);
						Thread.sleep(10);
						stringBuilder.replace(0, stringBuilder.length(), reader.readLine());
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
//				Publisher publisher = (Publisher)factory.getBean("publisher");
//				publisher.publishQuote("AMD", "333");
//				publisher.publishQuote("AMD", "111");
			}
		});
		
		exec.shutdown();
		//exec.awaitTermination(wait, TimeUnit.SECONDS);
	}
}
