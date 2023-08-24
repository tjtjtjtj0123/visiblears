package com.xcally.ars.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.xcally.ars.domain.Attach;
import com.xcally.ars.domain.Board;
import com.xcally.ars.domain.EmailMessage;
import com.xcally.ars.domain.crm.CRMApiCusRequest;
import com.xcally.ars.domain.crm.CRMApiCusResponse;
import com.xcally.ars.domain.crm.CRMApiMsgRequest;
import com.xcally.ars.domain.crm.CRMApiMsgResponse;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
    private  SpringTemplateEngine templateEngine;

	//게시글
    public void sendBoardMail(EmailMessage emailMessage, String type, Board board) {
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); 
            mimeMessageHelper.setSubject(emailMessage.getSubject()); 
            mimeMessageHelper.setText(setBoardContext(board, type), true); 
            javaMailSender.send(mimeMessage);   

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String setBoardContext(Board board, String type) {
        Context context = new Context();
        context.setVariable("boardSeq", 	board.getBoardSeq());
        context.setVariable("partner", 		board.getPartner());
        context.setVariable("title", 		board.getTitle());
        context.setVariable("content", 		board.getContent());
        context.setVariable("inquiryName", 	board.getInquiryName());
        context.setVariable("inquiryPhone", board.getInquiryPhone());
        context.setVariable("shopNo", 		board.getShopNo());
        context.setVariable("inquiryType", 	board.getInquiryType());
        context.setVariable("regDt", 		board.getRegDt());
        return templateEngine.process(type, context);
    }

	//첨부 이미지
    public void sendAttachdMail(EmailMessage emailMessage, String type, Attach attach) {
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); 
            mimeMessageHelper.setSubject(emailMessage.getSubject()); 
            mimeMessageHelper.setText(setAttachContext(attach, type), true); 
            javaMailSender.send(mimeMessage);   

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String setAttachContext(Attach attach, String type) {
        Context context = new Context();
        context.setVariable("attachSeq", 	attach.getAttachSeq());
        context.setVariable("boardSeq", 	attach.getBoardSeq());
        context.setVariable("partner", 	attach.getPartner());
        context.setVariable("s3Addr", 		attach.getS3Addr());
        context.setVariable("regDt", 		attach.getRegDt());
        return templateEngine.process(type, context);
    }
    
	//CRM MSG API 호출
    public void sendCrmMsgMail(EmailMessage emailMessage, String type, CRMApiMsgRequest crmApiMsgRequest, CRMApiMsgResponse crmApiMsgResponse) {
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); 
            mimeMessageHelper.setSubject(emailMessage.getSubject()); 
            mimeMessageHelper.setText(setCrmMsgContext(crmApiMsgRequest,crmApiMsgResponse, type), true); 
            javaMailSender.send(mimeMessage);   

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String setCrmMsgContext(CRMApiMsgRequest crmApiMsgRequest, CRMApiMsgResponse crmApiMsgResponse, String type) {
        Context context = new Context();
        //요청 crmApiRequest
        context.setVariable("partner", 		crmApiMsgRequest.getPartner());
        context.setVariable("comid", 		crmApiMsgRequest.getComid());
        context.setVariable("keycode", 		crmApiMsgRequest.getKeycode());
        context.setVariable("hp", 			crmApiMsgRequest.getHp());
        context.setVariable("title", 		crmApiMsgRequest.getTitle());
        
        context.setVariable("msg", 			crmApiMsgRequest.getMsg());
        context.setVariable("proctime", 	crmApiMsgRequest.getProctime());
        context.setVariable("seq", 			crmApiMsgRequest.getSeq());
        context.setVariable("fileNameList",	crmApiMsgRequest.getFileNameList());
        
        //응답 crmApiResponse
        context.setVariable("state", 		crmApiMsgResponse.getState());
        context.setVariable("code",			crmApiMsgResponse.getCode());
        context.setVariable("message",		crmApiMsgResponse.getMessage());

        return templateEngine.process(type, context);
    }

    
	//CRM CUS API 호출
    public void sendCrmCusMail(EmailMessage emailMessage, String type, CRMApiCusRequest crmApiCusRequest, CRMApiCusResponse crmApiCusResponse) {
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); 
            mimeMessageHelper.setSubject(emailMessage.getSubject()); 
            mimeMessageHelper.setText(setCrmCusContext(crmApiCusRequest,crmApiCusResponse, type), true); 
            javaMailSender.send(mimeMessage);   

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String setCrmCusContext(CRMApiCusRequest crmApiCusRequest,CRMApiCusResponse crmApiCusResponse, String type) {
        Context context = new Context();
        //요청 crmApiRequest
        context.setVariable("partner", 		crmApiCusRequest.getPartner());
        context.setVariable("comid", 		crmApiCusRequest.getComid());
        context.setVariable("keycode", 		crmApiCusRequest.getKeycode());
        context.setVariable("proc", 		crmApiCusRequest.getProc());
        context.setVariable("name", 		crmApiCusRequest.getName());
        
        context.setVariable("hp", 			crmApiCusRequest.getHp());
        context.setVariable("comname", 		crmApiCusRequest.getComname());
        context.setVariable("zipcode", 		crmApiCusRequest.getZipcode());
        context.setVariable("address",		crmApiCusRequest.getAddress());
        context.setVariable("memo",			crmApiCusRequest.getMemo());
        context.setVariable("seq",			crmApiCusRequest.getSeq());
        
        //응답 crmApiResponse
        context.setVariable("state", 		crmApiCusResponse.getState());
        context.setVariable("code",			crmApiCusResponse.getCode());
        context.setVariable("message",		crmApiCusResponse.getMessage());
        context.setVariable("custcode", 	crmApiCusResponse.getCustcode());
        context.setVariable("name", 		crmApiCusResponse.getName());
        context.setVariable("hp", 			crmApiCusResponse.getHp());
        return templateEngine.process(type, context);
    }

    
    
    //s3 등록 실패
	@Override
	public void sendS3Mail(EmailMessage emailMessage, String type, Long boardSeq, String file, String partner) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); 
            mimeMessageHelper.setSubject(emailMessage.getSubject()); 
            mimeMessageHelper.setText(setS3Context(type, boardSeq, file, partner), true); 
            javaMailSender.send(mimeMessage);   

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
	}

	@Override
	public String setS3Context(String type, Long boardSeq, String file, String partner) {
        Context context = new Context();
        context.setVariable("boardSeq", 	boardSeq.toString());
        context.setVariable("partner", 		partner);
        context.setVariable("file", 		file);
        return templateEngine.process(type, context);
	}
}