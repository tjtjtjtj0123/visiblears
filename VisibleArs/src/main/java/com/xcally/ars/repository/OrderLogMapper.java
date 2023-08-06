package com.xcally.ars.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.OrderLog;

@Mapper
public interface OrderLogMapper {
	public int InsOrderLog(OrderLog orderLog);
}
