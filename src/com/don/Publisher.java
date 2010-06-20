package com.don;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class Publisher {
	public void publishQuote(final String symbol, final String price) {
		jmsTemplate.send( 
				destination,
				new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						
						StringBuffer sb = new StringBuffer();
						sb.append("<updates>");
						sb.append("<update>");
						sb.append("<symbol>"+ symbol +"</symbol>" );
						sb.append("<price>" + price + "</price>");
						sb.append("<datetime>2006-09-20T13:59:25.993-04:00</datetime>" );
						sb.append("</update>");
						sb.append("</updates>");
						TextMessage message = session.createTextMessage(sb.toString());
						return message;
					}
				}
		);
	}
	
	private JmsTemplate jmsTemplate;
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	private Destination destination;
	public void setDestination(Destination destination ) {
		this.destination = destination;
	}
}
