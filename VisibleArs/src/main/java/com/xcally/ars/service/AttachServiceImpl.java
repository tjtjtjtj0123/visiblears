package com.xcally.ars.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xcally.ars.domain.Attach;
import com.xcally.ars.repository.AttachMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AttachServiceImpl implements AttachService{
	@Autowired
	private AttachMapper attachMmapper;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//첨부 문서 등록
	@Override
	public int  WriteAttach(Attach attach) {
		int rstl=0;
				
		try {
			rstl = attachMmapper.InsAttach(attach);
		}catch (Exception e) {
			logger.error("AttachServiceImpl -> WriteAttach -> "+getPrintStackTrace(e));
		}
		
		return rstl;
	}
	
	public static String getPrintStackTrace(Exception e) {
		  StringWriter errors = new StringWriter();
		  e.printStackTrace(new PrintWriter(errors));
		  return errors.toString();
	}
	
}
