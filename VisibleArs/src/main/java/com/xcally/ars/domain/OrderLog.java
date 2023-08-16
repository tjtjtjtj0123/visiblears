package com.xcally.ars.domain;
import java.time.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLog {
    private long 		  orderLogSeq; // 로그 번호
    private String 		  partner;     // 파트너
    private String 		  targetCnt;   //엑셀주문일자
    private String        sucCnt; 	   //성공개수
    private String 		  regYmd;	   // 주믄 등록일
    private LocalDateTime regDt; 	   // 배치 시간    
}
