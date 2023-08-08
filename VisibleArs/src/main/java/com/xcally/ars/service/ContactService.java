package com.xcally.ars.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.xcally.ars.domain.Attach;
import com.xcally.ars.domain.Partner;

public interface ContactService {
	public ResponseEntity<String> SearchCustomer();
	public ResponseEntity<String> RegCustomer();
	public ResponseEntity<String> Contact(Partner parnter, String hp, String title, String msg, String proctime, String seq, ArrayList<String> fileNameList);
}
