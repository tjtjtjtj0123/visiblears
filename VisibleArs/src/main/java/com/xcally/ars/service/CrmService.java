package com.xcally.ars.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.xcally.ars.domain.Attach;
import com.xcally.ars.domain.CRMApiRequest;
import com.xcally.ars.domain.Partner;

public interface CrmService {
	public ResponseEntity<String> RegMsg(CRMApiRequest crmApiRequest);
}
