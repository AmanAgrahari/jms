package com.virtualpairprogrammers.bookstore;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;

@Stateless
public class StockControlImplementation implements StockControlService {
	
	@Inject
	JMSContext context;
	
	@Resource(mappedName="java:jboss/exported/jms/BookStoreNotificationsQueue")
	Queue queue;

	public void registerNewStockItem(int productId, String productName,
			String category, Double unitPrice) throws SystemUnavailableException {
		
		try {
			MapMessage message = context.createMapMessage();
			message.setInt("productId", productId);
			message.setString("productName",productName);
			message.setString("category",category);
			message.setDouble("unitPrice",unitPrice);
			
			context.createProducer().send(queue, message);
		}
		catch (JMSException e) {
			throw new SystemUnavailableException();
		}

	}

}
