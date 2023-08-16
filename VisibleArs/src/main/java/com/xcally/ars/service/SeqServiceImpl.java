package com.xcally.ars.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xcally.ars.domain.Attach;
import com.xcally.ars.repository.AttachMapper;
import com.xcally.ars.repository.SeqMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SeqServiceImpl implements SeqService{
	@Autowired
	private SeqMapper seqMapper;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Long getSeq() {
		Map<String, Object> paramMap = new HashMap<>();
		Long generatedNumber = (long) 0;
		try {
			int seq = seqMapper.callUpKeyNtGen(paramMap);
			generatedNumber = (Long) paramMap.get("po_intKey");
		}catch (Exception e) {
			logger.error("SeqServiceImpl -> getSeq -> "+getPrintStackTrace(e));
		}
	
		return generatedNumber;
	}
	
	public static String getPrintStackTrace(Exception e) {
		  StringWriter errors = new StringWriter();
		  e.printStackTrace(new PrintWriter(errors));
		  return errors.toString();
	}
	
}
