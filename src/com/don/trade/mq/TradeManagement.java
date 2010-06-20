package com.don.trade.mq;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

public class TradeManagement extends NotificationBroadcasterSupport implements TradeManagementMBean {
	private long seqNo = 0;
	private String name;
	private ObjectName objName;
	
	public TradeManagement(String name) throws MalformedObjectNameException,
		InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
	{
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		this.name = "com.don.trade.mq:type=TradeManagement,name=" + name;
		objName = new ObjectName(this.name);
		server.registerMBean(this, objName);
	}
	
	public synchronized void notifyTrade(String xml) {
		this.seqNo++;
		Notification notification = new Notification(name, objName, this.seqNo, 
						System.currentTimeMillis(), xml);
		this.sendNotification(notification);
	}
}
