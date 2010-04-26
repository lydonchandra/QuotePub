package com.don;

import javax.jms.JMSException;
import javax.jms.TextMessage;

public class PureConsumer {
	public PureConsumer() {}
	
	public void processUpdate(String msg) throws JMSException {
		String str = msg;
		System.out.println("--" + msg);
		
	}
	
}
