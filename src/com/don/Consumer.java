package com.don;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;

public class Consumer {
	public Consumer() {}
	
	public String receiveUpdate() throws JMSException {
		TextMessage message  = (TextMessage)jmsTemplate.receive();
		String str = message.getText();
		
		return str;
	}
	
	private JmsTemplate jmsTemplate;
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
}
