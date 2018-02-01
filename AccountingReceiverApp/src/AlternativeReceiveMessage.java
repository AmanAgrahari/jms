import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class AlternativeReceiveMessage {

	public static void main(String[] args) {
		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		jndiProperties.put("jboss.naming.client.ejb.context","true");
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
		jndiProperties.put(Context.PROVIDER_URL,"http-remoting://localhost:8080");
		
		Connection connection = null;
		
		try {
			Context ctx = new InitialContext(jndiProperties);
			Queue queue = (Queue)ctx.lookup("jms/BookStoreQueue");
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("jms/RemoteConnectionFactory");
			connection = cf.createConnection();
			
//			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			//Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);	
			Session session = connection.createSession(true,Session.SESSION_TRANSACTED);
			MessageConsumer consumer = session.createConsumer(queue);
						
			connection.start();
			
		//fromline 42 to 59 it is for 100 message
			List<MapMessage> messages = new ArrayList<MapMessage>();
			
			while (messages.size() < 100) {
				MapMessage mapMessage = (MapMessage) consumer.receive(100);
				if (mapMessage != null) messages.add(mapMessage);
			}
			

			for (MapMessage mapMessage : messages) {
				String title = mapMessage.getString("title");
				int sku = mapMessage.getInt("sku");
				double price = mapMessage.getDouble("price");
				long longDate = mapMessage.getLong("date");
				Date date = new Date(longDate);
				
				System.out.println("Sale of " + title + " (" + sku + ") at $" + price + " on " + date);
			}
			/*
			MapMessage mapMessage = (MapMessage) consumer.receive(100);
			
			if (mapMessage != null ) {
				String title = mapMessage.getString("title");
				int sku = mapMessage.getInt("sku");
				double price = mapMessage.getDouble("price");
				long longDate = mapMessage.getLong("date");
				Date date = new Date(longDate);
				
				System.out.println("Sale of " + title + " (" + sku + ") at $" + price + " on " + date);
			mapMessage.acknowledge();
			}
			else {
				System.out.println("There were no messages");
			}*/
			
			session.commit();
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		finally {
			try {
				connection.close();
			} catch (JMSException e) {
				
			}
		}

	}

}
