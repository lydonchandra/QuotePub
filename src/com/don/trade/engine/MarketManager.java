package com.don.trade.engine;

import static java.lang.System.out;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

public class MarketManager implements Runnable, MessageListener {
	
	private static final String SYMBOL_TAG = "<symbol>";
	private static final String SYMBOL_END_TAG = "</symbol>";
	private static final String PRICE_TAG = "<price>";
	private static final String PRICE_END_TAG = "</price>";
	
	private boolean fDebug = true;
	
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageConsumer consumer = null;
	
	public static final int INITIAL_CAPACITY = 111;
	public Map<String, StringBuffer> marketBook = new HashMap<String,StringBuffer>(INITIAL_CAPACITY);
	Map donmap = new HashMap();
	       
	public MarketManager() {
	}
	
	public void close() {
		try {
			destination = null;
			consumer.close();
			session.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	private void setupJMS(String destinationName) throws Exception {
//		Context jndiContext = new InitialContext();
//		ConnectionFactory connectionFactory = (ConnectionFactory)jndiContext.lookup("jms/ConnectionFactory");
//		destination = (Destination)jndiContext.lookup(destinationName);
//		jndiContext.close();
//		
//		connection = connectionFactory.createConnection();
//		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//		consumer = session.createConsumer(destination);
//		consumer.setMessageListener( this );
//		connection.start();
//	}
	
	public void displayMarketBook() {
		System.out.println("************************************************");
		System.out.println("Current Market Book: ");
		Set<String> keys = marketBook.keySet();
		double price;
		for( String symbol : keys ) {
			price = getLastTradePrice(symbol);
			System.out.println(" Last trade price for " + symbol + " : " + price);
		}
	}
	
	public double getLastTradePrice(String symbol) {
		try {
			return Double.parseDouble( marketBook.get(symbol).toString());
		} catch (Exception e) {
		}
		
		return 0;
	}
	
	@Override
	public void run() {
		try {
			//setupJMS("jms/QuoteUpdates");
			final BeanFactory factory = new XmlBeanFactory(new FileSystemResource("/Users/lydonchandra/Documents/workspace-sts-carbon/QuotePub/src/com/don/donjms.xml"));
			//Publisher publisher = (Publisher)factory.getBean("publisher");
			DefaultMessageListenerContainer ml = (DefaultMessageListenerContainer)factory.getBean("jmsContainer2");
			ml.start();
			marketBook.put("a", new StringBuffer().append("aaaaaa"));
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Message msg) {
		try {
			TextMessage update = (TextMessage)msg;

			/*
             * SAMPLE UPDATE XML:
                <updates>
                    <update>
                        <symbol>SUNW</symbol>
                        <datetime>2006-09-20T13:59:25.993-04:00</datetime>
                        <price>4.9500</price>
                    </update>
                </updates>
            */

            // To preserve memory when running within a no-heap realtime thread
            // (NHRT) the XML String is walked manually, without the use of
            // a DOM or SAX parser that would otherwise create lots of objects
			String sUpdate = update.getText();
			int start = 0;
			boolean fParse = true;
			final int SYMBOL_START_LEN = SYMBOL_TAG.length();
			final int PRICE_START_LEN = PRICE_TAG.length();
			
			while (fParse) {
				int sBegin = sUpdate.indexOf(SYMBOL_TAG, start);
				if( sBegin < 0)
					break;
				
				int sEnd = sUpdate.indexOf(SYMBOL_END_TAG, sBegin);
				String symbol = sUpdate.substring(sBegin+SYMBOL_START_LEN, sEnd);
				
				int pBegin = sUpdate.indexOf(PRICE_TAG, start);
				int pEnd = sUpdate.indexOf(PRICE_END_TAG, pBegin);
				String price = sUpdate.substring(pBegin+PRICE_START_LEN, pEnd);
			
				start = pEnd;
				
				onUpdate(symbol, price);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onUpdate( String symbol, String price ) {
		log("Symbol: " + symbol + ", Quote: "  + price );
		StringBuffer sbPrice = marketBook.get(symbol);
		if( sbPrice == null ) {
			sbPrice = new StringBuffer(15);
			marketBook.put(symbol, sbPrice);
		}
		out.println("MarketBook " + marketBook.toString() + " size:" + marketBook.size());
		sbPrice.replace(0, price.length(), price);
	}
	
	private void log(String m) {
		if( fDebug )
			System.out.println(m);
	}
	
}
