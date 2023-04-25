package com.xcally.ars.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Partner {
    private int partnerSeq; // 파트너 고유번호
    private String partnerId; // 파트너 아이디
    private String partnerPw; // 파트너 패스워드
    private String partnerName; // 파트너 이름    
    private long seqNo; // 주문 고유 번호
    private String sabangNo; // 사방넷 주문번호
    private String shopNo; // 쇼핑몰 주문번호
    private LocalDateTime useDt; // 사용 가능 일자
    private LocalDateTime regDt; // 등록일자
}
