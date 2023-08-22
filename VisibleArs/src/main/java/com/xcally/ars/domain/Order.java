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
public class Order {
    private long seqNo; // 주문 번호
    private String partner; // 파트너
    private String mallOrderDt;//엑셀주문일자
    private String mall; //쇼핑몰
    private String orderDt;//주문일자
    
    private String sabangNo; // 사방넷 주문번호
    
    private String shopNo; // 쇼핑몰 주문번호
    private int payAmt; // 결제금액    
    private int quantity; // 수량
    private String productName; // 주문 상품명
    private String optionName;  // 옵션 명
    private String ordererName; // 주문자명
    
    private String ordererPhone1; // 주문자 연락처1
    private String ordererPhone2; // 주문자 연락처2   
    private String receiverName; // 수취인명
    private String receiverPhone1; // 수취인 연락처1
    private String receiverPhone2; // 수취인 연락처2
    
    private String receiverAddress; // 수취인 주소
    private String ordererEmail; // 주문자 이메일 주소
    private String ordField2;
    private String receiveZipcode;
    private LocalDateTime regDt; // 등록일자
}
