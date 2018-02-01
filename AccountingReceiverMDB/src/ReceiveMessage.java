import java.util.Date;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven (
		activationConfig = {
				@ActivationConfigProperty (propertyName="destinationType", propertyValue="javax.jms.Topic"),
				@ActivationConfigProperty( propertyName="destination", propertyValue="java:jboss/exported/jms/BookStoreTopic")
		}
		
		)

public class ReceiveMessage implements MessageListener {

	public void onMessage(Message message) {
		
		if (message instanceof MapMessage) {
			MapMessage mapMessage = (MapMessage) message;
			try {
				String title = mapMessage.getString("title");
				int sku = mapMessage.getInt("sku");
				double price = mapMessage.getDouble("price");
				long longDate = mapMessage.getLong("date");
				Date date = new Date(longDate);
				
				System.out.println("Attempting redelivery number " + mapMessage.getIntProperty("JMSXDeliveryCount"));
				System.out.println("Sale of " + title + " (" + sku + ") at $" + price + " on " + date);
				
				if (Math.random() < 0.9) {
				throw new SystemUnavailableException();
				}
				
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}
		else {
			throw new RuntimeException("Invalid message received");
		}

	}

}
