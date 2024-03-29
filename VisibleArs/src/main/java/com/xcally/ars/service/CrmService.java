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

public interface CrmService {
	public CRMApiMsgResponse RegMsg(CRMApiMsgRequest crmApiMsgRequest, Long board_seq);
	public CRMApiCusResponse RegCus(CRMApiCusRequest crmApiCusRequest, Long board_seq);
	//public CRMApiCusResponse GetCus(CRMApiCusRequest crmApiCusRequest);
}
