package com.github.pirent.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import com.github.pirent.api.OrderRepository;

/**
 * In-memory implementation for testing purpose only.
 * <p>
 * For the purpose of this example, let's assume that the
 * {@link OrderRepository} is being implemented by another team.
 * 
 * @author pirent
 *
 */
@Singleton
@Local(OrderRepository.class)
@Lock(LockType.READ)
public class SingletonOrderRepository implements OrderRepository {
	private List<List<String>> orders;
	
	@PostConstruct
	public void initialize() {
		orders = new ArrayList<List<String>>();
	}
	
	@Override
	@Lock(LockType.WRITE)
	public void addOrder(List<String> order) {
		orders.add(order);
	}

	@Override
	public List<List<String>> getOrders() {
		return Collections.unmodifiableList(orders);
	}

	@Override
	public int getOrderCount() {
		return orders.size();
	}

}
