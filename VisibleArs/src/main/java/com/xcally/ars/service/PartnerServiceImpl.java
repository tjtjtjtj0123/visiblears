package com.xcally.ars.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.xcally.ars.domain.Partner;
import com.xcally.ars.repository.PartnerMapper;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class PartnerServiceImpl implements PartnerService{
	@Autowired
	private PartnerMapper partnerMmapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	//Partner ID, PW 입력 후 로그인을 위한 
	@Override
	public Partner findPartner(Partner partner) {
		Partner objPartner = null;
		try {
			objPartner = partnerMmapper.findPartner(partner);	
		}catch (Exception e) {
			logger.error("PartnerServiceImpl -> findPartner -> "+getPrintStackTrace(e));
		}
		
		return objPartner;
	}
	
	//날짜 체크
	@Override
	public boolean findPartnerUseDt(String partnerId) {
		boolean rstl 		   = false;
		LocalDateTime datetime = null;
		
		try {
			String date = partnerMmapper.findPartnerUseDt(partnerId);
			
			if(date != null) {
				datetime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				if(datetime.isAfter(LocalDateTime.now())) {
					rstl=true;
				}	
			}	
		}catch (Exception e) {
			logger.error("PartnerServiceImpl -> findPartnerUseDt -> "+getPrintStackTrace(e));
		}
				
		return rstl;
	}
	
	public boolean updateSabangNo(Partner partner) {
		int rstl = 0;
		
		try {
			rstl = partnerMmapper.updateSabangNo(partner);
		}catch (Exception e) {
			logger.error("PartnerServiceImpl -> updateSabangNo -> "+getPrintStackTrace(e));
		}
		return rstl == 1 ? true : false;
	}
	
	@Override
	public Partner getPartnerInfo(String partnerId) {
		Partner objPartner = null;
		try {
			objPartner = partnerMmapper.getPartnerInfo(partnerId);
		}catch (Exception e) {
			logger.error("PartnerServiceImpl -> getPartnerInfo -> "+getPrintStackTrace(e));
		}
		return objPartner;
	}
	
	public static String getPrintStackTrace(Exception e) {
		  StringWriter errors = new StringWriter();
		  e.printStackTrace(new PrintWriter(errors));
		  return errors.toString();
	}
}
