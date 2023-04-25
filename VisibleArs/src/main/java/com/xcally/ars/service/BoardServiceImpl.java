package com.xcally.ars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.Attach;
import com.xcally.ars.domain.Board;
import com.xcally.ars.repository.PartnerMapper;
import com.xcally.ars.repository.AttachMapper;
import com.xcally.ars.repository.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService{
	@Autowired
	private BoardMapper boardMmapper;

	@Override
	public int WriteBoard(Board board) {
		int rstl=0;
		rstl = boardMmapper.InsBoard(board);
		
		return rstl;
	}
	
	
}
