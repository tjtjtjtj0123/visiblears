package com.xcally.ars.service;

import java.util.List;

import com.xcally.ars.domain.Order;

public interface OrderService {
	public Order getOrder(Order order);
	
	public List<Order> findOrderBySabangNo(int OrderNo);
	
	public void InsOrder(Order order);

}
