package com.don.web.jms;

import java.io.File;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class UploadJmsImpl {
	public UploadJmsImpl(){}
	private JmsTemplate jmsTemplate;
	
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
//	public void sendCsvFile(final List<Stock> stocks) {
//		jmsTemplate.send(	
//				new MessageCreator() {
//
//					@Override
//					public Message createMessage(Session session)
//							throws JMSException {
//						
//						MapMessage message = session.createMapMessage();
//						message.setString("symbol", stocks.get(0).getSymbol() );
//						return message;
//					}
//					
//				}
//		);
//	}
	
	public void sendCsvFile(final File file) {
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				MapMessage message = session.createMapMessage();
				message.setString("file" , file.getAbsolutePath());
				return message;
				
			}
		});
	}
	
	
	
	
}
