package com.xcally.ars.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcally.ars.domain.ApiLog;
import com.xcally.ars.domain.Attach;
import com.xcally.ars.domain.Board;
import com.xcally.ars.domain.EmailMessage;
import com.xcally.ars.domain.ExceptionLog;
import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.common.ExceptionUtils;
import com.xcally.ars.domain.common.S3Uploader;
import com.xcally.ars.domain.crm.CRMApiCusRequest;
import com.xcally.ars.domain.crm.CRMApiCusResponse;
import com.xcally.ars.domain.crm.CRMApiMsgRequest;
import com.xcally.ars.domain.crm.CRMApiMsgResponse;
import com.xcally.ars.service.ArsService;
import com.xcally.ars.service.AttachService;
import com.xcally.ars.service.BoardService;
import com.xcally.ars.service.CrmService;
import com.xcally.ars.service.EmailService;
import com.xcally.ars.service.LogService;
import com.xcally.ars.service.OrderService;
import com.xcally.ars.service.PartnerService;
import com.xcally.ars.service.SeqService;

import lombok.RequiredArgsConstructor;

public abstract class BaseController {
	@Autowired
	private OrderService orderservice;

	@Autowired
	private AttachService attachmentService;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private PartnerService partnerService;

	@Autowired
	private CrmService contactService;

	@Autowired
	private SeqService seqService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ArsService arsService;
	
	@Autowired
	private  S3Uploader s3Uploader;
	
	protected abstract String getPartner();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 메인메뉴
	@RequestMapping("menu") 
	public String menu(Model model,	@RequestParam(name= "token", required = false, defaultValue = "")String token)
	{
		//ARS호출
		if(token != null && !token.isEmpty() && !token.equals("null")) {
			Partner partner = partnerService.getPartnerInfo(getPartner());
			arsService.CallArs(token, "index", partner);			
		}
				
		model.addAttribute("partner", getPartner());
		return getPartner()+"/menu";
	}

	//인증페이지 메뉴
	@RequestMapping("auth") 
	public String auth(Model model,
		@RequestParam(name= "inquiryType",	required = false,	defaultValue = "") String inquiryType,
		@RequestParam(name= "token", 		required = false, 	defaultValue = "") String token) 
	{
		//ARS호출
		if(token != null && !token.isEmpty() && !token.equals("null")) {
			Partner partner = partnerService.getPartnerInfo(getPartner());
			arsService.CallArs(token, "service01", partner);			
		}
		
		model.addAttribute("inquiryType", inquiryType);		
		return getPartner()+"/auth";
	}
	
	//주문정보없음 메뉴
	@RequestMapping("noorder")
	public String order(Model model, 
			@RequestParam(name= "ordName",		required = false,	defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",		required = false,	defaultValue = "") String ordPhone, 
			@RequestParam(name= "recvAddr",		required = false,	defaultValue = "") String recvAddr,
			@RequestParam(name= "token", 		required = false, 	defaultValue = "") String token) 
	{
		//ARS호출
		if(token != null && !token.isEmpty() && !token.equals("null")) {
			Partner partner = partnerService.getPartnerInfo(getPartner());
			arsService.CallArs(token, "service02", partner);			
		}
		
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone);
		model.addAttribute("recvAddr",		recvAddr);
		
		return getPartner()+"/noorder";
	}
	
	//배송지변경 메뉴 
	@RequestMapping("changeShip") 
	public String changeShip(Model model, 
			@RequestParam(name= "ordName",		required = false,	defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",		required = false,	defaultValue = "") String ordPhone, 
			@RequestParam(name= "recvAddr",		required = false,	defaultValue = "") String recvAddr,
			@RequestParam(name= "shopNo",   	required = false,	defaultValue = "") String shopNo, 
			@RequestParam(name= "inquiryType",	required = false,	defaultValue = "") String inquiryType,
			@RequestParam(name= "token", 		required = false, 	defaultValue = "") String token) 
	{
		//ARS호출
		if(token != null && !token.isEmpty() && !token.equals("null")) {
			Partner partner = partnerService.getPartnerInfo(getPartner());
			arsService.CallArs(token, "service01", partner);			
		}
		
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone);
		model.addAttribute("recvAddr", 		recvAddr);
		model.addAttribute("shopNo", 		shopNo);
		model.addAttribute("inquiryType",	inquiryType);
		
		return getPartner()+"/changeShip";
	}

	//배송희망일요청 메뉴
	@RequestMapping("shipDate") 
	public String shipDate(Model model, 
			@RequestParam(name= "ordName",		required = false,	defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",		required = false,	defaultValue = "") String ordPhone, 
			@RequestParam(name= "recvAddr",		required = false,	defaultValue = "") String recvAddr,
			@RequestParam(name= "shopNo",		required = false,	defaultValue = "") String shopNo, 
			@RequestParam(name= "inquiryType",	required = false,	defaultValue = "") String inquiryType,
			@RequestParam(name= "token", 		required = false, 	defaultValue = "") String token) 
	{
		//ARS호출
		if(token != null && !token.isEmpty() && !token.equals("null")) {
			Partner partner = partnerService.getPartnerInfo(getPartner());
			arsService.CallArs(token, "service01", partner);			
		}
		
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone);
		model.addAttribute("recvAddr", 		recvAddr);
		model.addAttribute("shopNo", 		shopNo);
		model.addAttribute("inquiryType",	inquiryType);
		
		return getPartner()+"/shipDate";
	}

	//교환반품요청 메뉴
	@RequestMapping("requestExRe") 
	public String requestExRe(Model model, 
			@RequestParam(name= "ordName",		required = false,	defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",		required = false,	defaultValue = "") String ordPhone, 
			@RequestParam(name= "recvAddr",		required = false,	defaultValue = "") String recvAddr,
			@RequestParam(name= "shopNo",		required = false,	defaultValue = "") String shopNo, 
			@RequestParam(name= "inquiryType",	required = false,	defaultValue = "") String inquiryType,
			@RequestParam(name= "token", 		required = false, 	defaultValue = "") String token) 
	{
		//ARS호출
		if(token != null && !token.isEmpty() && !token.equals("null")) {
			Partner partner = partnerService.getPartnerInfo(getPartner());
			arsService.CallArs(token, "service01", partner);			
		}
		
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone);
		model.addAttribute("recvAddr", 		recvAddr);
		model.addAttribute("shopNo", 		shopNo);
		model.addAttribute("inquiryType",	inquiryType);
		
		return getPartner()+"/requestExRe";
	}	
	
	//주문취소 메뉴
	@RequestMapping("cancelOrder") 
	public String cancelOrder(Model model, 
			@RequestParam(name= "ordName",		required = false,	defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",		required = false,	defaultValue = "") String ordPhone, 
			@RequestParam(name= "recvAddr",		required = false,	defaultValue = "") String recvAddr,
			@RequestParam(name= "shopNo",		required = false,	defaultValue = "") String shopNo, 
			@RequestParam(name= "inquiryType",	required = false,	defaultValue = "") String inquiryType,
			@RequestParam(name= "token", 		required = false, 	defaultValue = "") String token) 
	{
		//ARS호출
		if(token != null && !token.isEmpty() && !token.equals("null")) {
			Partner partner = partnerService.getPartnerInfo(getPartner());
			arsService.CallArs(token, "service01", partner);			
		}
		
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone);
		model.addAttribute("recvAddr", 		recvAddr);
		model.addAttribute("shopNo", 		shopNo);
		model.addAttribute("inquiryType",	inquiryType);
		
		return getPartner()+"/cancelOrder";
	}	

	
	//범용문의 페이지
	@RequestMapping("inquiry") 
	public String inquiry(Model model, 
			@RequestParam(name= "shopNo",		required = false,	defaultValue = "") String shopNo,
			@RequestParam(name= "ordName",		required = false,	defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",		required = false,	defaultValue = "") String ordPhone,
			@RequestParam(name= "recvAddr",		required = false,	defaultValue = "") String recvAddr,
			@RequestParam(name= "inquiryType",	required = false,	defaultValue = "") String inquiryType,
			@RequestParam(name= "token", 		required = false, 	defaultValue = "") String token) 
	{		
		
		//ARS호출
		if(token != null && !token.isEmpty() && !token.equals("null")) {
			Partner partner = partnerService.getPartnerInfo(getPartner());
			arsService.CallArs(token, "service04", partner);			
		}
		
		model.addAttribute("shopNo", 		shopNo);
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone.replace(("-"), ""));
		model.addAttribute("recvAddr",		recvAddr);		
		model.addAttribute("inquiryType",	inquiryType);
		
		return getPartner()+"/inquiry";		
	}
	
	//범용문의 페이지
	@RequestMapping("inquiry2") 
	public String inquiry2(Model model, 
			@RequestParam(name= "shopNo",		required = false,	defaultValue = "") String shopNo,
			@RequestParam(name= "ordName",		required = false,	defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",		required = false,	defaultValue = "") String ordPhone,
			@RequestParam(name= "recvAddr",		required = false,	defaultValue = "") String recvAddr,
			@RequestParam(name= "inquiryType",	required = false,	defaultValue = "") String inquiryType,
			@RequestParam(name= "token", 		required = false, 	defaultValue = "") String token) 
	{		
		
		//ARS호출
		if(token != null && !token.isEmpty() && !token.equals("null")) {
			Partner partner = partnerService.getPartnerInfo(getPartner());
			arsService.CallArs(token, "service04", partner);			
		}
		
		model.addAttribute("shopNo", 		shopNo);
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone.replace(("-"), ""));
		model.addAttribute("recvAddr",		recvAddr);		
		model.addAttribute("inquiryType",	inquiryType);
		
		return getPartner()+"/inquiry2";		
	}

	//주문상세 페이지
	@RequestMapping("detail") 
	public String detail(Model model, 
			String shopNo, 
			@RequestParam(name= "ordererName",    	required = false,	defaultValue = "") String  ordererName,
			@RequestParam(name= "ordererPhone1",  	required = false,	defaultValue = "") String  ordererPhone1,
			@RequestParam(name= "receiverAddress",	required = false,	defaultValue = "") String  receiverAddress,
			@RequestParam(name= "inquiryType",	  	required = false,	defaultValue = "") String  inquiryType,
			@RequestParam(name= "token", 			required = false, 	defaultValue = "") String  token) 
	{
   
		List<Order> orderlist = null;		
		try {
			//ARS호출
			if(token != null && !token.isEmpty() && !token.equals("null")) {
				Partner partner = partnerService.getPartnerInfo(getPartner());
				arsService.CallArs(token, "service03", partner);			
			}
			
			orderlist = orderservice.findOrderByshopNo(shopNo);
			
			model.addAttribute("orderlist", orderlist);
			model.addAttribute("shopNo", 		shopNo);
			model.addAttribute("ordName", 		ordererName);
			model.addAttribute("ordPhone", 		ordererPhone1);
			model.addAttribute("recvAddr", 		receiverAddress);
			model.addAttribute("inquiryType",	inquiryType);		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getPartner()+"/detail";
	}
	
	
	//통화종료 ajax
	@PostMapping(value = "endArsAjax")
	@ResponseBody
	public int endArsAjax(@RequestParam(name = "token", required = false, defaultValue = "") String token) {

		try {
			if(token != null && !token.isEmpty() && !token.equals("null")){
				Partner partner = partnerService.getPartnerInfo(getPartner());
				arsService.EndArs(token, partner);	
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 1;
	}
	
	//상담원연결 ajax
	@PostMapping(value = "csrAjax")
	@ResponseBody
	public int csrAjax(@RequestParam(name = "token", required = false, defaultValue = "") String token) 
	{

		try {
			if(token != null && !token.isEmpty() && !token.equals("null")){
				Partner partner = partnerService.getPartnerInfo(getPartner());
				arsService.CallArs(token, "service00", partner);	
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 1;
	}	

	 //인증페이지 ajax
	@PostMapping(value = "get")
	@ResponseBody
	public Order getOrder(Order order) {
		Order rstl 		  = null;
		String addr 	  = "";
		String addr_tmp[] = null;
		try {

			//주소 맨 앞자 제거(서울특별시, 인천광역시 등)
			addr_tmp = order.getReceiverAddress().split(" ");
			for (int i = 1; i < addr_tmp.length; i++) {				
				addr += addr_tmp[i];
				if(i != addr_tmp.length-1)
					addr +=  " ";
			}
			
			order.setReceiverAddress(addr);
			order.setPartner(getPartner());			
			rstl = orderservice.getOrder(order);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}

	//1대1 문의하기
	@PostMapping("uploadimglist")
	@ResponseBody
	public int uploadlist(
			@RequestParam(name = "file",	 	 	required = false) List<MultipartFile> multipartFileList,
			@RequestParam(name = "title", 	     	required = true, 	defaultValue = "") String title,
			@RequestParam(name = "content",      	required = true, 	defaultValue = "") String content, 
			@RequestParam(name = "ordName",			required = true, 	defaultValue = "") String ordName,
			@RequestParam(name = "ordPhone",		required = true, 	defaultValue = "") String ordPhone, 
			@RequestParam(name = "inquiryType",		required = true, 	defaultValue = "") String inquiryType,
			@RequestParam(name = "shopNo",	 		required = false,	defaultValue = "") String shopNo)
	{
		int rstl 	  				   = 0;
		ArrayList<String> fileNameList = new ArrayList<String>();
		StringBuilder     titleSb 	   = new StringBuilder();
		Long 			  boardSeq     = seqService.getSeq();
		
		try {
			
			if(inquiryType.equals("getShip")) {
				titleSb.append("[배송조회]");
			}
			else if(inquiryType.equals("changeShip")) {
				titleSb.append("[배송지변경]");
			}
			else if(inquiryType.equals("shipDate")) {
				titleSb.append("[배송희망일요청]");
			}
			else if(inquiryType.equals("cancelOrder")) {
				titleSb.append("[주문취소]");
			}
			else if(inquiryType.equals("requestExRe")) {
				titleSb.append("[교환반품요청]");
			}
			else if(inquiryType.equals("ASRequest")) {
				titleSb.append("[AS요청]");
			}
			else if(inquiryType.equals("etc")) {
				titleSb.append("[기타문의]");
			}
			else {
				titleSb.append(inquiryType);
			}
			
			
			
			if(!shopNo.equals("")) {			
				titleSb.append("[쇼핑몰주문번호] "+shopNo +" ");
			}
			
			titleSb.append(title);			
			
			
			//게시글 등록
			regBoard(boardSeq,shopNo, inquiryType, titleSb.toString(), content, ordName, ordPhone);
				
			//첨부파일 등록
			fileNameList = regAttach(boardSeq, shopNo, multipartFileList);			

			//CRM 고객등록
			callRegCus(boardSeq, shopNo, ordName, ordPhone);
			
			//CRM 문자 전송
			callRegMsg(boardSeq, shopNo, inquiryType, titleSb.toString(), content, ordName, ordPhone, fileNameList);

      
			rstl = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}
	
	//배송지 변경 요청
	@PostMapping("changeShipAjax")
	@ResponseBody
	public int changeShipAjax(		
			@RequestParam(name = "content",      	required = true, 	defaultValue = "") String content, 
			@RequestParam(name = "ordName",  		required = true, 	defaultValue = "") String ordName,
			@RequestParam(name = "ordPhone",		required = true, 	defaultValue = "") String ordPhone, 
			@RequestParam(name = "inquiryType",  	required = true, 	defaultValue = "") String inquiryType,
			@RequestParam(name = "shopNo",	 		required = false,	defaultValue = "") String shopNo,
			@RequestParam(name = "recvAddr",	 	required = false,	defaultValue = "") String recvAddr,
			@RequestParam(name = "recvAddrDtl", 	required = false,	defaultValue = "") String recvAddrDtl)
	{
		int rstl 	  = 0;
		ArrayList<String> fileNameList = new ArrayList<String>();
		StringBuilder     titleSb      = new StringBuilder();
		StringBuilder     contentSb    = new StringBuilder();
		Long 			  boardSeq     = seqService.getSeq();
		
		try {
			
			titleSb.append("[배송지변경]");
			if(!shopNo.equals("")) {			
				titleSb.append("[쇼핑몰주문번호] "+shopNo +" ");
			}
			
			contentSb.append("[변경주소] ");
			contentSb.append(recvAddr+" ");
			contentSb.append(recvAddrDtl+" ");
			contentSb.append("[요청사항] "+content);
			
			//게시글 등록
			regBoard(boardSeq,shopNo, inquiryType, titleSb.toString(), contentSb.toString(), ordName, ordPhone);				
			
			//CRM 고객등록
			callRegCus(boardSeq, shopNo, ordName, ordPhone);
			
			//CRM 문자 전송
			callRegMsg(boardSeq, shopNo, inquiryType, titleSb.toString(), contentSb.toString(), ordName, ordPhone, fileNameList);

			rstl = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}	
	
	//배송지 희망일 요청
	@PostMapping("shipDateAjax")
	@ResponseBody
	public int shipDateAjax(		
			@RequestParam(name = "content",      	required = true, 	defaultValue = "") String content, 
			@RequestParam(name = "ordName",  		required = true, 	defaultValue = "") String ordName,
			@RequestParam(name = "ordPhone",		required = true, 	defaultValue = "") String ordPhone, 
			@RequestParam(name = "inquiryType",		required = true, 	defaultValue = "") String inquiryType,
			@RequestParam(name = "shopNo",	 		required = false,	defaultValue = "") String shopNo,
			@RequestParam(name = "datepicker",	 	required = false,	defaultValue = "") String datepicker)
	{
		int rstl 	  				   = 0;
		ArrayList<String> fileNameList = new ArrayList<String>();
		StringBuilder     titleSb      = new StringBuilder();
		StringBuilder     contentSb    = new StringBuilder();
		Long 			  boardSeq     = seqService.getSeq();
				
		try {
			titleSb.append("[배송희망일요청] ");					
			if(!shopNo.equals("")) {			
				titleSb.append("[쇼핑몰주문번호] "+shopNo +" ");
			}
			
			contentSb.append("[배송희망일자] " + datepicker + " ");
			contentSb.append("[요청사항] " + content);			
			
			//게시글 등록
			regBoard(boardSeq,shopNo, inquiryType, titleSb.toString(), contentSb.toString(), ordName, ordPhone);
						
			//CRM 고객등록
			callRegCus(boardSeq, shopNo, ordName, ordPhone);
			
			//CRM 문자 전송
			callRegMsg(boardSeq, shopNo, inquiryType, titleSb.toString(), contentSb.toString(), ordName, ordPhone, fileNameList);

			rstl = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}
	
	//주문 취소 요청
	@PostMapping("cancelOrderAjax")
	@ResponseBody
	public int cancelOrderAjax(		
			@RequestParam(name = "title", 	     	required = true, 	defaultValue = "") String title,
			@RequestParam(name = "content",      	required = true, 	defaultValue = "") String content, 
			@RequestParam(name = "ordName",  		required = true, 	defaultValue = "") String ordName,
			@RequestParam(name = "ordPhone",		required = true, 	defaultValue = "") String ordPhone, 
			@RequestParam(name = "inquiryType",		required = true, 	defaultValue = "") String inquiryType,
			@RequestParam(name = "shopNo",	 		required = false,	defaultValue = "") String shopNo)
	{
		int rstl 	  = 0;
		ArrayList<String> fileNameList = new ArrayList<String>();
		StringBuilder     titleSb   = new StringBuilder();
		StringBuilder     contentSb = new StringBuilder();
		Long 			  boardSeq  = seqService.getSeq();
		
		try {
			titleSb.append("[주문취소요청]");
			titleSb.append(title);
			if(!shopNo.equals("")) {			
				titleSb.append("[쇼핑몰주문번호] "+shopNo +" ");
			}
			
			contentSb.append("[사유] " + content);
									
			// 게시글 등록
			regBoard(boardSeq,shopNo, inquiryType, titleSb.toString(), contentSb.toString(), ordName, ordPhone);
						
			//CRM 고객등록
			callRegCus(boardSeq, shopNo, ordName, ordPhone);
			
			// CRM 문자 전송
			callRegMsg(boardSeq, shopNo, inquiryType, titleSb.toString(), contentSb.toString(), ordName, ordPhone, fileNameList);

			rstl = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}
	
	
	//교환반품 요청
	@PostMapping("requestExReAjax")
	@ResponseBody
	public int requestExReAjax(
			@RequestParam(name = "file",	 	 	required = false) List<MultipartFile> multipartFileList,
			@RequestParam(name = "content",      	required = true, 	defaultValue = "") String content, 
			@RequestParam(name = "ordName",			required = true, 	defaultValue = "") String ordName,
			@RequestParam(name = "ordPhone",		required = true, 	defaultValue = "") String ordPhone, 
			@RequestParam(name = "inquiryType",		required = true, 	defaultValue = "") String inquiryType,
			@RequestParam(name = "shopNo",	 		required = false,	defaultValue = "") String shopNo)
	{
		int rstl 	  				   = 0;
		ArrayList<String> fileNameList = new ArrayList<String>();
		StringBuilder     titleSb 	   = new StringBuilder();
		StringBuilder     contentSb    = new StringBuilder();
		Long 			  boardSeq     = seqService.getSeq();
		
		try {
			
			titleSb.append("[교환반품요청]");
			if(!shopNo.equals("")) {			
				titleSb.append("[쇼핑몰주문번호] "+shopNo +" ");
			}
			
			contentSb.append("[사유] " + content);
																	
			//게시글 등록
			regBoard(boardSeq,shopNo, inquiryType, titleSb.toString(), contentSb.toString(), ordName, ordPhone);
				
			//첨부파일 등록
			fileNameList = regAttach(boardSeq, shopNo, multipartFileList);			
			
			//CRM 고객등록
			callRegCus(boardSeq, shopNo, ordName, ordPhone);
			
			//CRM 문자 전송
			callRegMsg(boardSeq, shopNo, inquiryType, titleSb.toString(), contentSb.toString(), ordName, ordPhone, fileNameList);

			rstl = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}
	//게시글 등록
	public void regBoard(	
			@RequestParam(name= "boardSeq",			required = true,	defaultValue = "")	Long boardSeq,
			@RequestParam(name= "shopNo",			required = false,	defaultValue = "")	String shopNo,
			@RequestParam(name= "inquiryType",		required = false,	defaultValue = "")	String inquiryType,
			@RequestParam(name= "title",			required = false,	defaultValue = "")	String title,
			@RequestParam(name= "content",			required = false,	defaultValue = "") 	String content,			
			@RequestParam(name= "ordName",			required = true,	defaultValue = "")	String ordName,
			@RequestParam(name= "ordPhone",			required = true,	defaultValue = "")	String ordPhone) 
		{
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			
			Board board = Board.builder()
					.boardSeq(boardSeq)
					.shopNo(shopNo)
					.partner(getPartner())
					.title(title)
					.content(content)
					.inquiryName(ordName)
					.inquiryPhone(ordPhone)
					.inquiryType(inquiryType)
					.build();
			int rstl = boardService.WriteBoard(board);						

			//성공 1 실패 0 		//게시글 등록 실패시 로그등록 및 알림메일 발송
			if(rstl != 1) {
				
		        String jsonString = objectMapper.writeValueAsString(board);
		        
				ExceptionLog ex = ExceptionLog.builder()
									.partner(getPartner())
									.exceptionMessage("Board Regist Fail")
									.exceptionStackTrace(jsonString)
									.build();
				logService.InsExceptionLog(ex);
				
				EmailMessage emailMessage = EmailMessage.builder()
		                .to("eke6767@naver.com")
		                .subject("게시글 등록 실패")
		                .build();
		        emailService.sendBoardMail(emailMessage, "mail/boardmail", board);
			}
		}catch (Exception e) {
			logger.error(getPartner()+" -> regBoard -> "+ExceptionUtils.getPrintStackTrace(e));
		}
	}
	
	//첨부파일 등록
	public ArrayList<String> regAttach(
			@RequestParam(name = "boardSeq",	required = true,	defaultValue = "") Long boardSeq,
			@RequestParam(name = "shopNo",		required = false,	defaultValue = "") String shopNo,
			@RequestParam(name = "file",		required = false) 	List<MultipartFile> multipartFileList) 
	{
		ObjectMapper 	objectMapper   = new ObjectMapper();
		ArrayList<String> fileNameList = new ArrayList<String>();
		
		try {
			if(multipartFileList != null && multipartFileList.size()!=0){								
				for(MultipartFile file:multipartFileList)
				{
					String fileName = s3Uploader.uploadFiles(file, getPartner());
					
					if(fileName.contains("Fail")) {
						ExceptionLog ex = ExceptionLog.builder()
											.partner(getPartner())
											.exceptionMessage("File Regist Fail")
											.exceptionStackTrace(fileName)
											.build();
						logService.InsExceptionLog(ex);
						
						EmailMessage emailMessage = EmailMessage.builder()
				                .to("eke6767@naver.com")
				                .subject("S3 파일 등록 실패")
				                .build();

				        emailService.sendS3Mail(emailMessage, "mail/s3mail", boardSeq, fileName, getPartner());
					}
					else {
						Long attach_seq = seqService.getSeq();
						String cloudFront = "";
						if(fileName.contains("https://visiblears-s3.s3.ap-northeast-2.amazonaws.com")) {
							cloudFront = fileName.replace("https://visiblears-s3.s3.ap-northeast-2.amazonaws.com", "https://d1lwu4fbgnaq3z.cloudfront.net");
							fileNameList.add(cloudFront);
						}
						else {
							fileNameList.add(fileName);
						}
						
						Attach attach = Attach.builder()
								.attachSeq(attach_seq)
								.boardSeq(boardSeq)
								.shopNo(shopNo)
								.partner(getPartner())
								.s3Addr(fileName)
								.cloudFrontAddr(cloudFront)
								.build();
						
						int attach_ins_rstl = attachmentService.WriteAttach(attach);
						//성공 1 실패 0
						if(attach_ins_rstl != 1) {
							
					        String 		 jsonString2   = objectMapper.writeValueAsString(attach);					        
							ExceptionLog ex = ExceptionLog.builder()
												.partner(attach.getPartner())
												.exceptionMessage("Attach Regist Fail")
												.exceptionStackTrace(jsonString2)
												.build();
							logService.InsExceptionLog(ex);
							
							EmailMessage emailMessage = EmailMessage.builder()
					                .to("eke6767@naver.com")
					                .subject("첨부 문서 등록 실패")
					                .build();

					        emailService.sendAttachdMail(emailMessage, "mail/attachmail", attach);
						}
					}

				}
			}
		}catch (Exception e) {
			logger.error(getPartner()+" -> regAttach -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		return fileNameList;
	}
	
	//CRM REG MSG API 호출
	//이름 넣어야함 쇼핑몰 주문번호도넣어야함
	//boardseq 넣어야함
	public void callRegMsg(
			@RequestParam(name= "boardSeq",			required = true,	defaultValue = "")	Long boardSeq,
			@RequestParam(name= "shopNo",			required = false,	defaultValue = "")	String shopNo,
			@RequestParam(name= "inquiryType",		required = false,	defaultValue = "")	String inquiryType,
			@RequestParam(name= "title",			required = false,	defaultValue = "")	String title,
			@RequestParam(name= "content",			required = false,	defaultValue = "") 	String content,			
			@RequestParam(name= "ordName",			required = true,	defaultValue = "")	String ordName,
			@RequestParam(name= "ordPhone",			required = true,	defaultValue = "")	String ordPhone,
			@RequestParam(name= "fileNameList",		required = true,	defaultValue = "")	ArrayList<String> fileNameList)
	{
		
		try {
			Partner 		 Partner 	   = partnerService.getPartnerInfo(getPartner());
			Date 			 currentDate   = new Date();
	        SimpleDateFormat sdf           = new SimpleDateFormat("yyyyMMddHHmmss");	        
	        String           formattedDate = sdf.format(currentDate);

	        CRMApiMsgRequest crmApiRequest = CRMApiMsgRequest.builder()
	        						.partner(getPartner())
	        						.comid(Partner.getCrmCommid())
	        						.keycode(Partner.getCrmKeycode())
	        						.hp(ordPhone)
	        						.title(title)
	        						.msg(content)
	        						.proctime(formattedDate)
	        						.inquiryType(inquiryType)
	        						.seq(String.valueOf(seqService.getSeq()))
	        						.fileNameList(fileNameList)
	        						.build();
	        	        
	        //api 호출
	        CRMApiMsgResponse response  = contactService.RegMsg(crmApiRequest, boardSeq);
	        
	        if(!response.getState().equals("success")) {
	        	
	        	//실패시에만 메일 발송
				EmailMessage emailMessage = EmailMessage.builder()
		                .to("eke6767@naver.com")
		                .subject("CRM API 요청 실패")
		                .build();

		        emailService.sendCrmMsgMail(emailMessage, "mail/crmMsgmail", crmApiRequest, response);
	        }

		}catch (Exception e) {
			logger.error(getPartner()+" -> callRegMsg -> "+ExceptionUtils.getPrintStackTrace(e));
		}
	}
	
	//등록된 주문번호없을떄 예외처리 해야함 전부다
	//CRM REG MSG API 호출
	//이름 넣어야함 쇼핑몰 주문번호도넣어야함
	public void callRegCus(
			@RequestParam(name= "boardSeq",			required = true,	defaultValue = "")	Long boardSeq,
			@RequestParam(name= "shopNo",			required = false,	defaultValue = "")	String shopNo,
			@RequestParam(name= "ordName",			required = true,	defaultValue = "")	String ordName,
			@RequestParam(name= "ordPhone",			required = true,	defaultValue = "")	String ordPhone)
	{
		
		try {
			Date 			 currentDate   = new Date();
	        SimpleDateFormat sdf           = new SimpleDateFormat("yyyyMMddHHmmss");	        
	        String           formattedDate = sdf.format(currentDate);
			Partner Partner 	  		   = partnerService.getPartnerInfo(getPartner());
			CRMApiCusRequest crmApiCusRequest = null;
			
			
			if(shopNo.equals("")) {
				StringBuilder memoSb = new StringBuilder();				
				//memoSb.append("[문의일자]");
				//memoSb.append(formattedDate);						
				crmApiCusRequest = CRMApiCusRequest.builder()
													.partner(getPartner())
													.comid(Partner.getCrmCommid())
													.keycode(Partner.getCrmKeycode())
													.name(ordName)
													.hp(ordPhone)
													.seq(String.valueOf(seqService.getSeq()))
													.comname("")
													.zipcode("")
													.address("")
													.memo(memoSb.toString())
													.build();
			}
			else {
				List<Order> orderlist 		   = orderservice.findOrderByshopNo(shopNo);
				StringBuilder memoSb = new StringBuilder();
				
				//memoSb.append("[문의일자]");
				//memoSb.append(formattedDate);
				for(Order or : orderlist) {
					memoSb.append("[상품명]");
					memoSb.append(or.getProductName());			
					memoSb.append("[옵션]");
					memoSb.append(or.getOptionName());				
					memoSb.append("[수량]");
					memoSb.append(or.getQuantity());
					memoSb.append("[주문일자]");
					memoSb.append(or.getOrderDt());
					memoSb.append("    ");
				}
								
				crmApiCusRequest = CRMApiCusRequest.builder()
													.partner(getPartner())
													.comid(Partner.getCrmCommid())
													.keycode(Partner.getCrmKeycode())
													.name(ordName)
													.hp(ordPhone)
													.seq(String.valueOf(seqService.getSeq()))
													.comname(orderlist.get(0).getMall())
													.zipcode(orderlist.get(0).getReceiverZipcode())
													.address(orderlist.get(0).getReceiverAddress())
													.memo(memoSb.toString())
													.build();
			}		
			
	        CRMApiCusResponse response  = contactService.RegCus(crmApiCusRequest, boardSeq);
	        
	        if(!response.getState().equals("success")) {
	        	
	        	//실패시에만 메일 발송
				EmailMessage emailMessage = EmailMessage.builder()
		                .to("eke6767@naver.com")
		                .subject("CRM API 요청 실패")
		                .build();

		        emailService.sendCrmCusMail(emailMessage, "mail/crmCusmail", crmApiCusRequest, response);
	        }

		}catch (Exception e) {
			logger.error(getPartner()+" -> RegCus -> "+ExceptionUtils.getPrintStackTrace(e));
		}
	}
	/*
	 * 안쓰는 메뉴 및 함수
	 */
	
	/*
	@RequestMapping("sub") // 서브메뉴
	public String sub(Model model, String shopNo) {
		model.addAttribute("shopNo", shopNo);
		return getPartner()+"/sub";
	}
		
	@RequestMapping("call") // 기타 문의
	public String call() {		
		return getPartner()+"/call";
	}

*/
}
