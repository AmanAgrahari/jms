import java.util.Date;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;


public class MapMessageListener implements MessageListener {

	public void onMessage(Message message) {
		
		if (message instanceof MapMessage) {
			MapMessage mapMessage = (MapMessage) message;
			try {
				String title = mapMessage.getString("title");
				int sku = mapMessage.getInt("sku");
				double price = mapMessage.getDouble("price");
				long longDate = mapMessage.getLong("date");
				Date date = new Date(longDate);
				
				System.out.println("Sale of " + title + " (" + sku + ") at $" + price + " on " + date);
				
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
			
			
		}
		else {
			System.out.println("Invalid message received.");
		}

	}

}
