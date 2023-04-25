package com.xcally.ars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xcally.ars.domain.Attach;
import com.xcally.ars.repository.AttachMapper;

@Service
public class AttachServiceImpl implements AttachService{
	@Autowired
	private AttachMapper attachMmapper;

	@Override
	public int  WriteAttach(Attach attach) {
		int rstl=0;
		rstl = attachMmapper.InsAttach(attach);
		
		return rstl;
	}
	
	
}
