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
public class Board {
    private Long boardSeq; // 문의글번호
    private String shopNo; // 쇼핑몰 주문번호
    private String partner; // 파트너사
    private String title; // 제목
    private String content; // 내용
    private String inquiryName; // 문의자 명
    private String inquiryPhone; // 문의자 연락처
    private String mallOrderDt; // 엑셀 주문일자
    private String inquiryType; //문의타입
    private LocalDateTime regDt; // 등록일자
}
