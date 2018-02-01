import java.util.Enumeration;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class Monitor1
{
	public static void main(String[] args)
	{

		Connection connection = null;
		Session session = null;

		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		jndiProperties.put("jboss.naming.client.ejb.context",true);
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
		jndiProperties.put(Context.PROVIDER_URL,"http-remoting://localhost:8080");

		
		try
		{
			Context ctx = new InitialContext(jndiProperties);

			Queue queue = (Queue)ctx.lookup("jms/DLQ");

			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("jms/RemoteConnectionFactory");

			connection = cf.createConnection(); 

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			connection.start();
			
			QueueBrowser browser = session.createBrowser(queue);

			
			Boolean finished = false;
			while (!finished) {
				
				Enumeration<Message> e = browser.getEnumeration();
				int numMsgs =0;
			
				while (e.hasMoreElements()) {
					Message message = e.nextElement();
					MapMessage mapMessage = (MapMessage) message;
					
					Enumeration<String> names =  mapMessage.getMapNames();
					System.out.println("Message number " + numMsgs);
					while (names.hasMoreElements()) {
						String name = names.nextElement();
						Object value = mapMessage.getObject(name);
						System.out.println(name + ":" + value);
					}
					
					numMsgs++;
				}
				System.out.println("There are currently " + numMsgs + " messages in the queue.");
				
				System.out.println("Press enter to refresh or x to exit");
				String input = System.console().readLine();
				if (input.toLowerCase().trim().equals("x")) {
					finished = true;
				}
			}
			
		}
		catch (Exception e)
		{
			System.out.println(e);
		}

		finally {
			try
			{
				connection.close();
			} catch (JMSException e)
			{
				e.printStackTrace();
			} 
		}


	}
}
