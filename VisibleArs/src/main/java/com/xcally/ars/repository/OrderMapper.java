package com.xcally.ars.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xcally.ars.domain.Order;

@Mapper
public interface OrderMapper {
	public Order findOrder(Order order);
	public List<Order> findOrderBySabangNo(int OrderNo);
	public int InsOrder(Order order);
	
	public Order case1(Order order);
}
