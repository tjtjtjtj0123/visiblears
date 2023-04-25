package com.xcally.ars.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Attach {
	private int attachSeq;
	private int boardSeq;	
	private String s3Addr;
	private LocalDateTime regDt; 				// 등록 일자
	
}
