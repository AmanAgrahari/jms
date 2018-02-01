package com.virtualpairprogrammers.bookstore;

import javax.ejb.Remote;

@Remote
public interface StockControlService {

	public void registerNewStockItem(int productId, String productName, String category, Double unitPrice) throws SystemUnavailableException;
}
