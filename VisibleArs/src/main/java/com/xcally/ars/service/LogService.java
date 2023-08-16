package com.xcally.ars.service;

import com.xcally.ars.domain.ApiLog;
import com.xcally.ars.domain.ExceptionLog;
import com.xcally.ars.domain.OrderLog;

public interface LogService {
	public int InsOrderLog(OrderLog orderLog);			//주문 정보 등록 로그
	public int InsApiLog(ApiLog apiLog);				//api 호출 로그
	public int UpdApiLog(ApiLog apiLog);
	public int InsExceptionLog(ExceptionLog exceptionLog);	//Exception 로그
}
