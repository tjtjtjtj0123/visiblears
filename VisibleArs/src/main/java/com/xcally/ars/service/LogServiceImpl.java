package com.xcally.ars.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xcally.ars.domain.ApiLog;
import com.xcally.ars.domain.Attach;
import com.xcally.ars.domain.ExceptionLog;
import com.xcally.ars.domain.OrderLog;
import com.xcally.ars.domain.common.ExceptionUtils;
import com.xcally.ars.repository.AttachMapper;
import com.xcally.ars.repository.LogMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LogServiceImpl implements LogService{
	@Autowired
	private LogMapper orderLogMapper;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//첨부 문서 등록
	@Override
	public int InsOrderLog(OrderLog orderLog) {
		int rstl=0;
				
		try {
			rstl = orderLogMapper.InsOrderLog(orderLog);
		}catch (Exception e) {
			logger.error("LogService -> InsOrderLog -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
		return rstl;
	}

	@Override
	public int InsApiLog(ApiLog apiLog) {
		int rstl=0;
		
		try {
			rstl = orderLogMapper.InsApiLog(apiLog);
		}catch (Exception e) {
			logger.error("LogService -> InsApiLog -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
		return rstl;
	}

	@Override
	public int InsExceptionLog(ExceptionLog exceptionLog) {
		int rstl=0;
		
		try {
			rstl = orderLogMapper.InsExceptionLog(exceptionLog);
		}catch (Exception e) {
			logger.error("LogService -> InsExceptionLog -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
		return rstl;
	}


	@Override
	public int UpdApiLog(ApiLog apiLog) {
		int rstl=0;
		
		try {
			rstl = orderLogMapper.UpdApiLog(apiLog);
		}catch (Exception e) {
			logger.error("LogService -> UpdApiLog -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
		return rstl;
	}

}
