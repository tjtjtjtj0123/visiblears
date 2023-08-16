package com.xcally.ars.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xcally.ars.domain.ApiLog;
import com.xcally.ars.domain.ExceptionLog;
import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.OrderLog;

@Mapper
public interface LogMapper {
	public int InsOrderLog(OrderLog orderLog);
	public int InsApiLog(ApiLog apiLog);
	public int UpdApiLog(ApiLog apiLog);
	public int InsExceptionLog(ExceptionLog exceptionLog);
}
