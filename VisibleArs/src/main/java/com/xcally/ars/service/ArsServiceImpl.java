package com.xcally.ars.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcally.ars.domain.ApiLog;
import com.xcally.ars.domain.EmailMessage;
import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.common.ExceptionUtils;
import com.xcally.ars.domain.crm.CRMApiCusRequest;
import com.xcally.ars.domain.crm.CRMApiCusResponse;
import com.xcally.ars.domain.crm.CRMApiMsgRequest;
import com.xcally.ars.domain.crm.CRMApiMsgResponse;

@Service
public class ArsServiceImpl implements ArsService{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LogService logService; 


	@Override
	public void CallArs(String token, String proc,Partner partner) {

		try {
			// API URL 및 데이터 준비
	        String url     = partner.getArsUrl()  + "/ServiceAPI/ViewARS/";

	        // HttpHeaders 설정
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	        // form 데이터 설정
	        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

	        formData.add("token", 		token);
	        formData.add("keycode", 	partner.getArsKeycode());
	        formData.add("proc", 		proc);     
	        
	        // RestTemplate을 사용하여 API 호출
	        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
	        RestTemplate restTemplate = new RestTemplate();
	        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		}catch (Exception e) {
			logger.error("ArsServiceImpl -> CallArs -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
        // API 호출 결과 반환
        return;
	}

	@Override
	public void EndArs(String token,Partner partner) {
		try {
			// API URL 및 데이터 준비
	        String url     = partner.getArsUrl()  + "/ServiceAPI/ViewARSHangup/index.html";

	        // HttpHeaders 설정
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	        // form 데이터 설정
	        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

	        formData.add("token", 		token);
	        formData.add("keycode", 	partner.getArsKeycode());	        

	        // RestTemplate을 사용하여 API 호출
	        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
	        RestTemplate restTemplate = new RestTemplate();
	        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		}catch (Exception e) {
			logger.error("ArsServiceImpl -> CallArs -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
        // API 호출 결과 반환
        return;
	}
	
}
