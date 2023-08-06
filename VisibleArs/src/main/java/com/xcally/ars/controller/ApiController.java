package com.xcally.ars.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.PartnerLogin;
import com.xcally.ars.domain.common.Result;
import com.xcally.ars.domain.common.SimpleResult;
import com.xcally.ars.service.PartnerService;

import lombok.Builder;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
@RestController
public class ApiController {
	
    @Value("${api.crmurl}")
    private String apiUrl;
    
	@Autowired
	private PartnerService partnerservice;
	
    @GetMapping("/callApi")
    public ResponseEntity<String> callApi() {
        // API URL 및 데이터 준비
        String url = apiUrl  + "/ServiceAPI/CustomerEdit/";
        String comid = "xcally";
        String keycode = "pYeJ2kT6raDZZwINn7BFwfccwvPkh1h9JzbV6tPRjJo=";

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // form 데이터 설정
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("comid", comid);
        formData.add("keycode", keycode);

        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        // RestTemplate을 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // API 호출 결과 반환
        return responseEntity;
    }
    
	//test
    @GetMapping("/members")
    public List<Map<String, Object>> findAllMember() {
        List<Map<String, Object>> members = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Map<String, Object> member = new HashMap<>();
            member.put("id", i);
            member.put("name", i + "번 개발자");
            member.put("age", 10 + i);
            members.add(member);
        }
        return members;
    }   	

	@PostMapping("/updatesabangno")
	public ResponseEntity<SimpleResult> updateSabangNo(@RequestBody Map<String, Object> request) {
		SimpleResult simpleResult = new SimpleResult();
		String partnerId 		  = (String) request.get("partnerId");
		String sabangNo     	  = (String) request.get("sabangNo");
	    Partner objPartner 		  = null;
	    
	    
        objPartner = Partner.builder()
    			.sabangNo(sabangNo)
    			.partnerId(partnerId)
    			.build();
    
        boolean rstl = partnerservice.updateSabangNo(objPartner);
		if(rstl) {
			simpleResult.ErrCode=0;
			simpleResult.ErrMsg="성공";
		}
		else {
			simpleResult.ErrCode=999;
			simpleResult.ErrMsg="실패";
		}
        
		return new ResponseEntity<>(simpleResult, null, HttpStatus.SC_OK);
	}
	
    
    @PostMapping("/api2")
    public ResponseEntity<Result<String>> chkPartner(@RequestBody PartnerLogin partnerLogin) {
    	Result<String> rstl = new Result<String>();
		String partner_id = null;
		boolean useDtFlag = false;
		System.out.println();
		Partner pl_objParnter = Partner.builder()
								.partnerId(partnerLogin.getPartnerId())
								.partnerPw(partnerLogin.getPartnerPw())
								.build();
    	
		//아이디 패스워드 체크 //기간 체크		
		pl_objParnter = partnerservice.findPartner(pl_objParnter);

		//사방넷 번호 가져오기
		if(pl_objParnter == null) {
			rstl.errCode=999;
			rstl.errMsg="계정정보가 없습니다.";
			return new ResponseEntity<>(rstl, null, HttpStatus.SC_OK);
		} 
		
		//사용기한 제크
		useDtFlag = partnerservice.findPartnerUseDt(pl_objParnter.getPartnerId());
		
		if(!useDtFlag) {
			rstl.errCode=998;
			rstl.errMsg="사용기한이 지났습니다.";		
			return new ResponseEntity<>(rstl, null, HttpStatus.SC_OK);
		}
		rstl.data = pl_objParnter.getSabangNo();
		rstl.errMsg="ok";
    	return new ResponseEntity<>(rstl, null, HttpStatus.SC_OK);
    			
    }
}
