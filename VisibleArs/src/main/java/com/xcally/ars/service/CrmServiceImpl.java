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
public class CrmServiceImpl implements CrmService{
    
	@Value("${api.crmurl}")
    private String apiUrl;
    
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LogService logService; 

	//문자 등록
	@Override
	public CRMApiMsgResponse RegMsg(CRMApiMsgRequest crmApiMsgRequest) {

		ResponseEntity<String> responseEntity = null;
		ObjectMapper objectMapper = new ObjectMapper();
		CRMApiMsgResponse response = CRMApiMsgResponse.builder()
									.state("fail")
									.code("999")
									.message("")
									.build();
		try {
			// API URL 및 데이터 준비
	        String url     = apiUrl  + "/ServiceAPI/MsgRegister/";

	        // HttpHeaders 설정
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	        // form 데이터 설정
	        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

	        formData.add("comid", 		crmApiMsgRequest.getComid());
	        formData.add("keycode", 	crmApiMsgRequest.getKeycode());
	        formData.add("hp", 			crmApiMsgRequest.getHp());     
	        formData.add("title", 		crmApiMsgRequest.getTitle());
	        formData.add("msg", 		crmApiMsgRequest.getMsg());
	        formData.add("proctime",  	crmApiMsgRequest.getProctime());
	        formData.add("seq", 		crmApiMsgRequest.getSeq());
        
	        if(crmApiMsgRequest.getFileNameList().size() > 0 ) {
	        	for(int i = 0;i<crmApiMsgRequest.getFileNameList().size();i++) {
	        		formData.add("imglink"+(i+1), crmApiMsgRequest.getFileNameList().get(i));
	        	}
	        }
	        
	        String apiRequest = objectMapper.writeValueAsString(formData);
	        
	        //REQ 데이터 LOGGING
	        ApiLog Request = ApiLog.builder()
	        				.apiLogSeq(Long.parseLong(crmApiMsgRequest.getSeq()))
	        				.partner(crmApiMsgRequest.getPartner())
	        				.endPoint(url)
	        				.request(apiRequest)
	        				.build();
	        logService.InsApiLog(Request);


	        // RestTemplate을 사용하여 API 호출
	        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
	        RestTemplate restTemplate = new RestTemplate();
	        responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
	        
	        response  = objectMapper.readValue(responseEntity.getBody(), CRMApiMsgResponse.class);
	        
	        String 		     jsonString = objectMapper.writeValueAsString(response);	 
	        
	        //성공 시
	        if(response.getState().equals("success") && responseEntity.getStatusCode().is2xxSuccessful()) {
	        	ApiLog Response = ApiLog.builder()
		        				.partner(crmApiMsgRequest.getPartner())
		        				.errorMessage("")
		        				.success(true)
		        				.response(jsonString)
		        				.apiLogSeq(Long.parseLong(crmApiMsgRequest.getSeq()))
		        				.build();
	        	logService.UpdApiLog(Response);
	        	
	        }else {
	        	ApiLog Response = ApiLog.builder()
		        				.partner(crmApiMsgRequest.getPartner())
		        				.errorMessage("")
		        				.success(false)
		        				.response(jsonString)
		        				.apiLogSeq(Long.parseLong(crmApiMsgRequest.getSeq()))
		        				.build();
	        	logService.UpdApiLog(Response);
	        }	        
		}catch (Exception e) {
			logger.error("CrmServiceImpl -> RegMsg -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
        // API 호출 결과 반환
        return response;
	}
	
	//고객 등록
	@Override
	public CRMApiCusResponse RegCus(CRMApiCusRequest crmApiCusRequest) {

		ResponseEntity<String> responseEntity = null;
		ObjectMapper objectMapper = new ObjectMapper();
		CRMApiCusResponse response = CRMApiCusResponse.builder()
									.state("fail")
									.code("999")
									.build();
		try {
			// API URL 및 데이터 준비
	        String url     = apiUrl  + "/ServiceAPI/CustomerEdit/";

	        // HttpHeaders 설정
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	        // form 데이터 설정
	        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

	        formData.add("comid", 		crmApiCusRequest.getComid());
	        formData.add("keycode", 	crmApiCusRequest.getKeycode());	        
	        formData.add("proc", 		"UI");     
	        formData.add("name", 		crmApiCusRequest.getName());
	        formData.add("hp", 			crmApiCusRequest.getHp());
	        formData.add("comname", 	crmApiCusRequest.getComname());
	        formData.add("zipcode", 	crmApiCusRequest.getZipcode());
	        formData.add("address", 	crmApiCusRequest.getAddress());
	        formData.add("memo", 		crmApiCusRequest.getMemo());
        
	        
	        String apiRequest = objectMapper.writeValueAsString(formData);
	        
	        //REQ 데이터 LOGGING
	        ApiLog Request = ApiLog.builder()
	        				.apiLogSeq(Long.parseLong(crmApiCusRequest.getSeq()))
	        				.partner(crmApiCusRequest.getPartner())
	        				.endPoint(url)
	        				.request(apiRequest)
	        				.build();
	        logService.InsApiLog(Request);


	        // RestTemplate을 사용하여 API 호출
	        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
	        RestTemplate restTemplate = new RestTemplate();
	        responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
	        
	        response  = objectMapper.readValue(responseEntity.getBody(), CRMApiCusResponse.class);
	        
	        String 		     jsonString = objectMapper.writeValueAsString(response);	 
	        
	        //성공 시
	        if(response.getState().equals("success") && responseEntity.getStatusCode().is2xxSuccessful()) {
	        	ApiLog Response = ApiLog.builder()
		        				.partner(crmApiCusRequest.getPartner())
		        				.errorMessage("")
		        				.success(true)
		        				.response(jsonString)
		        				.apiLogSeq(Long.parseLong(crmApiCusRequest.getSeq()))
		        				.build();
	        	logService.UpdApiLog(Response);
	        	
	        }else {
	        	ApiLog Response = ApiLog.builder()
		        				.partner(crmApiCusRequest.getPartner())
		        				.errorMessage("")
		        				.success(false)
		        				.response(jsonString)
		        				.apiLogSeq(Long.parseLong(crmApiCusRequest.getSeq()))
		        				.build();
	        	logService.UpdApiLog(Response);
	        }	        
		}catch (Exception e) {
			logger.error("CrmServiceImpl -> RegCus -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
        // API 호출 결과 반환
        return response;
	}
	
}
