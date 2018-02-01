import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class ReceiveMessage {
	
	private static boolean lookForUserPressingX() {
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine().toLowerCase().equals("x");
	}

	public static void main(String[] args) {
		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		jndiProperties.put("jboss.naming.client.ejb.context","true");
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
		jndiProperties.put(Context.PROVIDER_URL,"http-remoting://localhost:8080");
		
		Connection connection = null;
		
		try {
			Context ctx = new InitialContext(jndiProperties);
			//Queue queue = (Queue)ctx.lookup("jms/BookStoreQueue");
			Topic topic = (Topic) ctx.lookup("jms/BookStoreTopic");
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("jms/RemoteConnectionFactory");
			connection = cf.createConnection();
			connection.setClientID("accounting1");
			
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			
			//MessageConsumer consumer = session.createConsumer(topic);
			MessageConsumer consumer = session.createDurableConsumer(topic, "accounting");
			//consumer.setMessageListener(new TextMessageListener());MapMessageListener
			consumer.setMessageListener(new MapMessageListener());
			connection.start();
			
			boolean finished = false;
			while (!finished) {
				finished = lookForUserPressingX();
			}
			
			
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
