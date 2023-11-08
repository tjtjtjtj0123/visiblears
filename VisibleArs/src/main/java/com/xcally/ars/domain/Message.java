package com.xcally.ars.domain;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    String corpNum;    // 팝빌회원 사업자번호
    String userID;    // 팝빌회원 아이디
    String sender;    // 발신번호
    String receiver;    // 수신번호
    String receiverName;    // 수신자명
    String content;    // 메시지 내용, 90byte 초과된 내용은 삭제되어 전송
    Date reserveDT;    // 예약전송일시, null 처리시 즉시전송
    Boolean adsYN;    // 광고문자 전송여부
    String requestNum;
    // 전송요청번호
    // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
    // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
}
