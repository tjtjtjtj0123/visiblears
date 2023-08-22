package com.xcally.ars.domain.crm;

import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CRMApiCusRequest {
	private String partner;
    private String comid;
    private String keycode; 
    private String proc;//(default I) I-등록, U-수정,    D-삭제(상담이력포함 삭제)    UI-등록,키값 중복시 수정(키값-고객명,휴대전화)    IC-등록+CRM(Empty)
    private String name;
    private String hp;
    private String comname;//회사명 (주문쇼핑몰)
    private String zipcode;
    private String address;
    private String memo;
    private String seq;

}