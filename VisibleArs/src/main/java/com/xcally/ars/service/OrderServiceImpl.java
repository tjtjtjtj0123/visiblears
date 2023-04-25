package com.xcally.ars.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xcally.ars.domain.Order;
import com.xcally.ars.repository.OrderMapper;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderMapper ordermapper;
	
	@Override
	public Order getOrder(Order order) {
		
		Order objOrder = ordermapper.findOrder(order); 
		
		return objOrder;
	}

	@Override
	public List<Order> findOrderBySabangNo(int OrderNo) {
		
		List<Order> objOrderList = ordermapper.findOrderBySabangNo(OrderNo);
		
		return objOrderList;
	}

	@Override
	public void InsOrder(Order order) {
		ordermapper.InsOrder(order);		
	}

}
