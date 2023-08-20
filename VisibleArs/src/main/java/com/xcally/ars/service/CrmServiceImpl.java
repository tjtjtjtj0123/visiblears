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
import com.xcally.ars.domain.CRMApiMsgRequest;
import com.xcally.ars.domain.CRMApiMsgResponse;
import com.xcally.ars.domain.EmailMessage;
import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.common.ExceptionUtils;

@Service
public class CrmServiceImpl implements CrmService{
    
	@Value("${api.crmurl}")
    private String apiUrl;
    
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LogService logService; 

	@Autowired
	private EmailService emailService;
	//문자 등록
	@Override
	public ResponseEntity<String> RegMsg(CRMApiMsgRequest crmApiRequest) {

		ResponseEntity<String> responseEntity = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// API URL 및 데이터 준비
	        String url     = apiUrl  + "/ServiceAPI/MsgRegister/";

	        // HttpHeaders 설정
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	        // form 데이터 설정
	        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

	        formData.add("comid", 		crmApiRequest.getComid());
	        formData.add("keycode", 	crmApiRequest.getKeycode());
	        formData.add("hp", 			crmApiRequest.getHp());     
	        formData.add("title", 		crmApiRequest.getTitle());
	        formData.add("msg", 		crmApiRequest.getMsg());
	        formData.add("proctime",  	crmApiRequest.getProctime());
	        formData.add("seq", 		crmApiRequest.getSeq());
        
	        if(crmApiRequest.getFileNameList().size() > 0 ) {
	        	for(int i = 0;i<crmApiRequest.getFileNameList().size();i++) {
	        		formData.add("imglink"+(i+1), crmApiRequest.getFileNameList().get(i));
	        	}
	        }
	        
	        String apiRequest = objectMapper.writeValueAsString(formData);
	        
	        //REQ 데이터 LOGGING
	        ApiLog Request = ApiLog.builder()
	        				.apiLogSeq(Long.parseLong(crmApiRequest.getSeq()))
	        				.partner(crmApiRequest.getPartner())
	        				.endPoint(url)
	        				.request(apiRequest)
	        				.build();
	        logService.InsApiLog(Request);


	        // RestTemplate을 사용하여 API 호출
	        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
	        RestTemplate restTemplate = new RestTemplate();
	        responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
	                
		}catch (Exception e) {
			logger.error("PartnerServiceImpl -> Contact -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
        // API 호출 결과 반환
        return responseEntity;
	}
	
	//content 만들기
	public String makeContent(CRMApiMsgRequest crmApiRequest) {
		/*
		getShip     : 배송 조회
		changeShip : 배송지 변경
		ShipDate : 배송 희망일 요청
		cancelOrder 주문 취소
		requestExRe 교환 반품 요청	
		*/
		
		
		
		return"";
	}
	
}
