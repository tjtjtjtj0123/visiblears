package com.xcally.ars.service;

import org.springframework.http.ResponseEntity;

import com.xcally.ars.domain.Attach;
import com.xcally.ars.domain.Partner;

public interface ContactService {
	public ResponseEntity<String> SearchCustomer();
	public ResponseEntity<String> RegCustomer();
	public ResponseEntity<String> Contact(Partner parnter, String hp, String msg, String proctime, String seq);
}
