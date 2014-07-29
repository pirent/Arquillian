package com.github.pirent.api;

import java.util.List;

public interface OrderRepository {

	void addOrder(List<String> order);
	
	List<List<String>> getOrders();
	
	int getOrderCount();
}
