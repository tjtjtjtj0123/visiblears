package com.xcally.ars.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attach {
	private Long attachSeq;
	private Long boardSeq;	
    private String partner; 		// 파트너사
    private String sabangNo; // 사방넷 주문번호
	private String s3Addr;
	private String cloudFrontAddr;
	private LocalDateTime regDt;	// 등록 일자
	
}
