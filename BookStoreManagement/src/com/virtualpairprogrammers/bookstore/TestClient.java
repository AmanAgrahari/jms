package com.virtualpairprogrammers.bookstore;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TestClient
{
	public static void main(String[] args) throws NamingException {
		
		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		jndiProperties.put(Context.PROVIDER_URL,"http-remoting://localhost:8080");
		jndiProperties.put("jboss.naming.client.ejb.context", true);
		
		Context jndi = new InitialContext(jndiProperties);
		
		StockControlService service = (StockControlService)jndi.lookup("BookstoreStockManagement/StockControlImplementation!com.virtualpairprogrammers.bookstore.StockControlService");
		
		try
		{
			service.registerNewStockItem(1, "Test Product", "Books", 3.99);
		} catch (SystemUnavailableException e)
		{
			System.out.println("The queue was unavailable");
		}
	}

}
