package com.xcally.ars.service;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xcally.ars.domain.Order;
import com.xcally.ars.repository.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderMapper ordermapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Override
	public Order getOrder(Order order) {
		
		Order objOrder = null;
		try {
			objOrder = ordermapper.findOrder(order); 
		}catch (Exception e) {
			logger.error("OrderServiceImpl -> getOrder -> "+getPrintStackTrace(e));
		}
		
		return objOrder;
	}

	@Override
	public List<Order> findOrderBySabangNo(int OrderNo) {
		List<Order> objOrderList = null;
		
		try {
			objOrderList = ordermapper.findOrderBySabangNo(OrderNo);
		}catch (Exception e) {
			logger.error("OrderServiceImpl -> findOrderBySabangNo -> "+getPrintStackTrace(e));
		}
				
		return objOrderList;
	}

	@Override
	public void InsOrder(Order order) {
		
		try {
			ordermapper.InsOrder(order);
		}catch (Exception e) {
			logger.debug("PartnerServiceImpl -> InsOrder -> "+getPrintStackTrace(e));
		}

	}
	public static String getPrintStackTrace(Exception e) {
		  StringWriter errors = new StringWriter();
		  e.printStackTrace(new PrintWriter(errors));
		  return errors.toString();
	}
}
