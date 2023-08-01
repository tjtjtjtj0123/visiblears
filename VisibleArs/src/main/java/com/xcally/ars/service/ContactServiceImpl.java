package com.xcally.ars.service;

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

import com.xcally.ars.domain.Partner;

@Service
public class ContactServiceImpl implements ContactService{
    
	@Value("${api.baseurl}")
    private String apiUrl;
    
	//고객 검색
	@Override
	public ResponseEntity<String> SearchCustomer() {
        // API URL 및 데이터 준비
        String url     = apiUrl  + "/ServiceAPI/Customer/";
        String comid   = "xcally";
        String keycode = "0bs6h0h3jybk75cv7xwxq7oxg6a5x9uh";

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // form 데이터 설정
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("comid", comid);
        formData.add("keycode", keycode);
        formData.add("custcode", "");
        formData.add("custname", "");
        formData.add("hp", "");
        formData.add("phone", "");
        //formData.add("page", "");
        //formData.add("rows", "");
        
        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        // RestTemplate을 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // API 호출 결과 반환
        return responseEntity;
	}

	//고객관리(등록, 수정, 삭제)
	@Override
	public ResponseEntity<String> RegCustomer() {
        // API URL 및 데이터 준비
        String url     = apiUrl  + "/ServiceAPI/CustomerEdit/";
        String comid   = "xcally";
        String keycode = "0bs6h0h3jybk75cv7xwxq7oxg6a5x9uh";

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

	//문자 등록
	@Override
	public ResponseEntity<String> Contact(Partner parnter, String hp, String msg, String proctime, String seq) {
        // API URL 및 데이터 준비
        String url     = apiUrl  + "/ServiceAPI/MsgRegister/";
        String comid   = "xcally";
        String keycode = "0bs6h0h3jybk75cv7xwxq7oxg6a5x9uh";

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // form 데이터 설정
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("comid", parnter.getCommid());
        formData.add("keycode", parnter.getKeycode());
        formData.add("hp", hp);
        formData.add("msg", msg);
        formData.add("procitme",  proctime);
        formData.add("seq", proctime);

        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        // RestTemplate을 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // API 호출 결과 반환
        return responseEntity;
	}

}
