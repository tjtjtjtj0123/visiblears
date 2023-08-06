package com.xcally.ars.service;

import java.util.List;

import com.xcally.ars.domain.Order;

public interface OrderService {
	public Order getOrder(Order order);
	
	public int InsOrder(Order order);
	
	public Order getOrderDecode(Order order);
	
	public int InsOrderEncode(Order order);
	
	public List<Order> findOrderBySabangNo(int OrderNo);
	

}
