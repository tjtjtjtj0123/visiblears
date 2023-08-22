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
    
	public void sendCrmMail(EmailMessage emailMessage, String type, CRMApiMsgRequest crmApiRequest, CRMApiMsgResponse crmApiResponse);
    public String setCrmContext(CRMApiMsgRequest crmApiRequest,CRMApiMsgResponse crmApiResponse, String type);
    
    public void sendS3Mail(EmailMessage emailMessage, String type, Long boardSeq, String file, String partner);
    public String setS3Context(String type, Long boardSeq, String file, String partner);
}