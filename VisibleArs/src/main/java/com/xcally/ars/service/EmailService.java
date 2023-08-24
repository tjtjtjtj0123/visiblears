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
public interface EmailService {

	public void sendBoardMail(EmailMessage emailMessage, String type, Board board);
    public String setBoardContext(Board board, String type);
    
	public void sendAttachdMail(EmailMessage emailMessage, String type, Attach attach);
    public String setAttachContext(Attach attach, String type);
    
	public void sendCrmMsgMail(EmailMessage emailMessage, String type, CRMApiMsgRequest crmApiMsgRequest, CRMApiMsgResponse crmApiMsgResponse);
    public String setCrmMsgContext(CRMApiMsgRequest crmApiMsgRequest,CRMApiMsgResponse crmApiMsgResponse, String type);
    
	public void sendCrmCusMail(EmailMessage emailMessage, String type, CRMApiCusRequest crmApiCusRequest, CRMApiCusResponse crmApiCusResponse);
    public String setCrmCusContext(CRMApiCusRequest crmApiCusRequest,CRMApiCusResponse crmApiCusResponse, String type);
    
    public void sendS3Mail(EmailMessage emailMessage, String type, Long boardSeq, String file, String partner);
    public String setS3Context(String type, Long boardSeq, String file, String partner);
}