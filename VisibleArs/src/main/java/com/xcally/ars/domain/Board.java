package com.xcally.ars.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Board {
    private int boardSeq; // 문의글번호
    private String partner; // 파트너사
    private String title; // 제목
    private String content; // 내용
    private String inquiryName; // 문의자 명
    private String inquiryPhone; // 문의자 연락처
    private String mallOrderDt; // 엑셀 주문일자
    private String inquiryType; //문의타입
    private LocalDateTime regDt; // 등록일자
}
