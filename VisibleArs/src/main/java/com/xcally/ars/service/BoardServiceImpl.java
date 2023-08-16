package com.xcally.ars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.common.ExceptionUtils;
import com.xcally.ars.domain.Attach;
import com.xcally.ars.domain.Board;
import com.xcally.ars.domain.EmailMessage;
import com.xcally.ars.repository.PartnerMapper;
import com.xcally.ars.repository.AttachMapper;
import com.xcally.ars.repository.BoardMapper;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BoardServiceImpl implements BoardService{
	@Autowired
	private BoardMapper boardMmapper;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	//게시글 등록
	@Override
	public int WriteBoard(Board board) {
		int rstl = 0;
		
		try {
			rstl = boardMmapper.InsBoard(board);

		}catch (Exception e) {
			logger.error("BoardServiceImpl -> WriteBoard -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		
		return rstl;
	}
}
