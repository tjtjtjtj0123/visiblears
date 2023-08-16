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
import com.xcally.ars.domain.CRMApiRequest;
import com.xcally.ars.domain.CRMApiResponse;
import com.xcally.ars.domain.EmailMessage;
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
        context.setVariable("mallOrderDt", 	board.getMallOrderDt());
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
    
	//CRM API 호출
    public void sendCrmMail(EmailMessage emailMessage, String type, CRMApiRequest crmApiRequest, CRMApiResponse crmApiResponse) {
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); 
            mimeMessageHelper.setSubject(emailMessage.getSubject()); 
            mimeMessageHelper.setText(setCrmContext(crmApiRequest,crmApiResponse, type), true); 
            javaMailSender.send(mimeMessage);   

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String setCrmContext(CRMApiRequest crmApiRequest, CRMApiResponse crmApiResponse, String type) {
        Context context = new Context();
        //요청 crmApiRequest
        context.setVariable("partner", 		crmApiRequest.getPartner());
        context.setVariable("comid", 		crmApiRequest.getComid());
        context.setVariable("keycode", 		crmApiRequest.getKeycode());
        context.setVariable("hp", 			crmApiRequest.getHp());
        context.setVariable("title", 		crmApiRequest.getTitle());
        
        context.setVariable("msg", 			crmApiRequest.getMsg());
        context.setVariable("proctime", 	crmApiRequest.getProctime());
        context.setVariable("seq", 			crmApiRequest.getSeq());
        context.setVariable("fileNameList",	crmApiRequest.getFileNameList());
        
        //응답 crmApiResponse
        context.setVariable("state", 		crmApiResponse.getState());
        context.setVariable("code",			crmApiResponse.getCode());
        context.setVariable("message",		crmApiResponse.getMessage());

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