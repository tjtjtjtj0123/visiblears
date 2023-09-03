package com.xcally.ars.service;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.common.EncryptionService;
import com.xcally.ars.domain.common.ExceptionUtils;
import com.xcally.ars.repository.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderMapper ordermapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EncryptionService encryptionService; 
	
	//암호화 X
	@Override
	public Order getOrder(Order order) {
		
		Order objOrder = null;
		try {
			if(order.getPartner().equals("therapedic")) {
				order.setPartner("englander");
			}
			
			objOrder = ordermapper.findOrder(order); 
		}catch (Exception e) {
			logger.error("OrderServiceImpl -> getOrder -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
		return objOrder;
	}
	
	//암호화 X
	@Override
	public int InsOrder(Order order) {
		int rstl = 0;
		try {
			rstl = ordermapper.InsOrder(order);
		}catch (Exception e) {
			logger.debug("PartnerServiceImpl -> InsOrder -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		return rstl;
	}

	//암호화 O
	@Override
	public int InsOrderEncode(Order order) {
		int rstl = 0;
		try {
            Order encOrder = Order.builder()
    				.partner(order.getPartner())
    				.sabangNo(order.getSabangNo())
    				.shopNo(order.getShopNo())
    				.payAmt(order.getPayAmt())        				
    				.quantity(order.getQuantity())
    				.productName(order.getProductName())
    				.ordererName(encryptionService.encrypt(order.getOrdererName()))
    				.ordererPhone1(encryptionService.encrypt(order.getOrdererPhone1()))
    				.ordererPhone2(encryptionService.encrypt(order.getOrdererPhone2()))
    				.receiverName(encryptionService.encrypt(order.getReceiverName()))
    				.receiverPhone1(encryptionService.encrypt(order.getReceiverPhone1()))
    				.receiverPhone2(encryptionService.encrypt(order.getReceiverPhone2()))
    				.receiverAddress(encryptionService.encrypt(order.getReceiverAddress()))	
    				.ordererEmail(encryptionService.encrypt(order.getOrdererEmail()))
    				.mallOrderDt(order.getMallOrderDt())
    				.mall(order.getMall())
    				.orderDt(order.getOrderDt())
    				.build();
			rstl = ordermapper.InsOrder(encOrder);
		}catch (Exception e) {
			logger.debug("PartnerServiceImpl -> InsOrder -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		return rstl;
	}
	
	@Override
	public List<Order> findOrderByshopNo(String shopNo) {
		List<Order> objOrderList = null;
		
		try {
			objOrderList = ordermapper.findOrderByshopNo(shopNo);
		}catch (Exception e) {
			logger.error("OrderServiceImpl -> findOrderBySabangNo -> "+ExceptionUtils.getPrintStackTrace(e));
		}
				
		return objOrderList;
	}

	@Override
	public Order getOrderDecode(Order order) {
		// TODO Auto-generated method stub
		return null;
	}
}
