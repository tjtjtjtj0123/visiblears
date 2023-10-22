package com.xcally.ars.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.popbill.api.MessageService;
import com.popbill.api.PopbillException;

@Controller
public class MessageServiceController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "sendSMS", method = RequestMethod.GET)
    public String sendSMS(Model m) {

        // 팝빌회원 사업자번호
        String corpNum = "6028800920";

        // 팝빌회원 아이디
        String userID = "choi@xcally.co.kr";

        // 발신번호
        String sender = "07043042991";

        // 수신번호
        String receiver = "01080077296";

        // 수신자명
        String receiverName = "수신자명";

        // 메시지 내용, 90byte 초과된 내용은 삭제되어 전송
        String content = "문자메시지 내용";

        // 예약전송일시, null 처리시 즉시전송
        Date reserveDT = null;

        // 광고문자 전송여부
        Boolean adsYN = false;

        // 전송요청번호
        // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
        // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
        String requestNum = "";

        try {

            String receiptNum = messageService.sendSMS(corpNum, sender, receiver,
            receiverName, content, reserveDT, adsYN, userID, requestNum);

            m.addAttribute("Result", receiptNum);

        } catch (PopbillException e) {
            // 예외 발생 시, e.getCode() 로 오류 코드를 확인하고, e.getMessage()로 오류 메시지를 확인합니다.
            System.out.println("오류 코드" + e.getCode());
            System.out.println("오류 메시지" + e.getMessage());
        }

        return "response";
    }
}