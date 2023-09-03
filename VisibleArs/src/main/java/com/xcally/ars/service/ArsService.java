package com.xcally.ars.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.xcally.ars.domain.Attach;
import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.crm.CRMApiCusRequest;
import com.xcally.ars.domain.crm.CRMApiCusResponse;
import com.xcally.ars.domain.crm.CRMApiMsgRequest;
import com.xcally.ars.domain.crm.CRMApiMsgResponse;

public interface ArsService {
	public void CallArs(String token,String proc, Partner partner);
	public void EndArs(String token, Partner partner);
}
