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
import com.xcally.ars.domain.CRMApiRequest;
import com.xcally.ars.domain.CRMApiResponse;
import com.xcally.ars.domain.EmailMessage;
import com.xcally.ars.domain.ExceptionLog;
import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.common.ExceptionUtils;
import com.xcally.ars.domain.common.S3Uploader;
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
	private  S3Uploader s3Uploader;
	
	protected abstract String getPartner();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("menu") // 메인메뉴
	public String menu(Model model)
	{
		model.addAttribute("partner", getPartner());
		return getPartner()+"/menu";
	}

	@RequestMapping("changeShip") //배송지 변경 
	public String changeaddr(Model model, 
			@RequestParam(name= "ordName",required = false,defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",required = false,defaultValue = "") String ordPhone, 
			@RequestParam(name= "recvAddr",required = false,defaultValue = "") String recvAddr,
			@RequestParam(name= "sabangNo",required = false,defaultValue = "") String sabangNo, 
			@RequestParam(name= "inquiryType",required = false,defaultValue = "") String inquiryType) {
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone);
		model.addAttribute("recvAddr", 	recvAddr);
		model.addAttribute("sabangNo", sabangNo);
		model.addAttribute("inquiryType",inquiryType);
		return getPartner()+"/changeShip";
	}

	@RequestMapping("ShipDate") //배송희망일 요청
	public String changedate(Model model, 
			@RequestParam(name= "ordName",required = false,defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",required = false,defaultValue = "") String ordPhone, 
			@RequestParam(name= "recvAddr",required = false,defaultValue = "") String recvAddr,
			@RequestParam(name= "sabangNo",required = false,defaultValue = "") String sabangNo, 
			@RequestParam(name= "inquiryType",required = false,defaultValue = "") String inquiryType) {
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone);
		model.addAttribute("recvAddr", 	recvAddr);
		model.addAttribute("sabangNo", sabangNo);
		model.addAttribute("inquiryType",inquiryType);
		return getPartner()+"/ShipDate";
	}

	@RequestMapping("requestExRe") //교환 반품 요청
	public String requestExRe(Model model, 
			@RequestParam(name= "ordName",required = false,defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",required = false,defaultValue = "") String ordPhone, 
			@RequestParam(name= "recvAddr",required = false,defaultValue = "") String recvAddr,
			@RequestParam(name= "sabangNo",required = false,defaultValue = "") String sabangNo, 
			@RequestParam(name= "inquiryType",required = false,defaultValue = "") String inquiryType) {
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone);
		model.addAttribute("recvAddr", 	recvAddr);
		model.addAttribute("sabangNo", sabangNo);
		model.addAttribute("inquiryType",inquiryType);
		return getPartner()+"/requestExRe";
	}	
	
	@RequestMapping("cancelOrder") //주문 취소
	public String cancelOrder(Model model, 
			@RequestParam(name= "ordName",required = false,defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",required = false,defaultValue = "") String ordPhone, 
			@RequestParam(name= "recvAddr",required = false,defaultValue = "") String recvAddr,
			@RequestParam(name= "sabangNo",required = false,defaultValue = "") String sabangNo, 
			@RequestParam(name= "inquiryType",required = false,defaultValue = "") String inquiryType) {
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone);
		model.addAttribute("recvAddr", 	recvAddr);
		model.addAttribute("sabangNo", sabangNo);
		model.addAttribute("inquiryType",inquiryType);
		return getPartner()+"/cancelOrder";
	}	
	
	@RequestMapping("sub") // 서브메뉴
	public String sub(Model model, String sabangNo) {
		model.addAttribute("sabangNo", sabangNo);
		return getPartner()+"/sub";
	}

	@RequestMapping("auth") // 인증페이지
	public String auth(Model model,
		@RequestParam(name= "inquiryType",required = false,defaultValue = "") String inquiryType) {
		model.addAttribute("inquiryType", inquiryType);
		return getPartner()+"/auth";
	}

	@RequestMapping("noorder")//주문정보없음
	public String order(Model model, 
			@RequestParam(name= "ordName",required = false,defaultValue = "") String ordName,
			@RequestParam(name= "ordPhone",required = false,defaultValue = "") String ordPhone, 
			@RequestParam(name= "recvAddr",required = false,defaultValue = "") String recvAddr) {
		
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone);
		model.addAttribute("recvAddr", 	recvAddr);
		return getPartner()+"/noorder";
	}
	

	@RequestMapping("inquiry") // 기타 문의
	public String inquiry(Model model, 
			@RequestParam(name= "sabangNo",required = false,defaultValue = "") String  sabangNo,
			@RequestParam(name= "ordName",required = false,defaultValue = "") String  ordName,
			@RequestParam(name= "ordPhone",required = false,defaultValue = "") String  ordPhone,
			@RequestParam(name= "recvAddr",required = false,defaultValue = "") String  recvAddr,
			@RequestParam(name= "inquiryType",required = false,defaultValue = "") String inquiryType) {
		
		model.addAttribute("sabangNo", 			sabangNo);
		model.addAttribute("ordName", 		ordName);
		model.addAttribute("ordPhone",		ordPhone.replace(("-"), ""));
		model.addAttribute("recvAddr",	recvAddr);		
		model.addAttribute("inquiryType", 		inquiryType);
		return getPartner()+"/inquiry";		
	}
	
	@RequestMapping("call") // 기타 문의
	public String call() {		
		return getPartner()+"/call";
	}

	@RequestMapping("detail") // 주문상세
	public String detail(Model model, int sabangNo, 
			@RequestParam(name= "ordName",    required = false,	defaultValue = "") String  ordName,
			@RequestParam(name= "ordPhone",  required = false,	defaultValue = "") String  ordPhone,
			@RequestParam(name= "recvAddr",required = false,	defaultValue = "") String  recvAddr,
			@RequestParam(name= "inquiryType",	  required = false,	defaultValue = "") String  inquiryType) {
   
		List<Order> orderlist     			 = null;
		List<Order> destOrderlist            = new ArrayList<Order>();
		HashMap<String, Integer> quantityMap = new HashMap<>();		
		
		try {

			model.addAttribute("sabangNo", 			sabangNo);
			model.addAttribute("ordName", 		ordName);
			model.addAttribute("ordPhone", 	ordPhone);
			model.addAttribute("recvAddr", 	recvAddr);
			model.addAttribute("inquiryType", 		inquiryType);
			
			orderlist = orderservice.findOrderBySabangNo(sabangNo);
			
			for (int i = 0; i < orderlist.size(); i++) {
				//상품명과 주소를 키값으로
				String tmp = orderlist.get(i).getProductName()+"::::"+orderlist.get(i).getReceiverAddress();
				if(!quantityMap.containsKey(tmp)) {
					quantityMap.put(tmp, orderlist.get(i).getQuantity());

				}else {
					int tmpQuantity = quantityMap.get(tmp);
					quantityMap.replace(tmp, orderlist.get(i).getQuantity()+tmpQuantity);
				}				
			}

			for(String key:quantityMap.keySet()) {
				Order tmpOrder = Order.builder()
									.quantity(quantityMap.get(key))
									.productName(key.split("::::")[0])
									.receiverAddress(key.split("::::")[1])									
									.build();
																	
				destOrderlist.add(tmpOrder);		
			}
			
			model.addAttribute("orderlist", destOrderlist);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return getPartner()+"/detail";
	}

	
	@PostMapping(value = "get") // 인증페이지 ajax
	@ResponseBody
	public Order getOrder(Order order) {
		Order rstl 		  = null;
		String addr 	  = "";
		String addr_tmp[] = null;
		try {

			// 1.주소 맨 앞자 제거(서울특별시, 인천광역시 등)
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
			@RequestParam(name = "file",	 	 required = false, 	defaultValue = "") List<MultipartFile> multipartFileList,
			@RequestParam(name = "title", 	     required = true, 	defaultValue = "") String title,
			@RequestParam(name = "content",      required = true, 	defaultValue = "") String content, 
			@RequestParam(name = "ordName",  required = true, 	defaultValue = "") String ordName,
			@RequestParam(name = "ordPhone",required = true, 	defaultValue = "") String ordPhone, 
			@RequestParam(name = "inquiryType",  required = true, 	defaultValue = "") String inquiryType,
			@RequestParam(name = "sabangNo",	 required = false,	defaultValue = "") String sabangNo) throws IOException	
	{
		int rstl 	  = 0;
		ArrayList<String> fileNameList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		try {
			
			//3. msg 내용 만들기
			if(inquiryType.equals("getShip")) {
				sb.append("[배송조회]");
			}
			else if(inquiryType.equals("changeShip")) {
				sb.append("[배송지변경]");
			}
			else if(inquiryType.equals("ShipDate")) {
				sb.append("[배송희망일요청]");
			}
			else if(inquiryType.equals("cancelOrder")) {
				sb.append("[주문취소]");
			}
			else if(inquiryType.equals("requestExRe")) {
				sb.append("[교환반품요청]");
			}
			else if(inquiryType.equals("ASRequest")) {
				sb.append("[AS요청]");
			}
			sb.append(content);
			title = "[제목]:" + title;
			Long boardSeq = seqService.getSeq();
			//1. 게시글 등록
			regBoard(boardSeq,sabangNo, inquiryType, title, sb.toString(), ordName, ordPhone);
				
			//2. 첨부파일 등록
			fileNameList = regAttach(boardSeq, sabangNo, multipartFileList);			

			
			//4. CRM 문자 전송
			callCRMApi(boardSeq, sabangNo, inquiryType, title, sb.toString(), ordName, ordPhone, fileNameList);

			rstl = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}
	
	//배송지 변경 요청
	@PostMapping("changeAddrAjax")
	@ResponseBody
	public int changeAddrAjax(		
			@RequestParam(name = "content",      required = true, 	defaultValue = "") String content, 
			@RequestParam(name = "ordName",  required = true, 	defaultValue = "") String ordName,
			@RequestParam(name = "ordPhone",required = true, 	defaultValue = "") String ordPhone, 
			@RequestParam(name = "inquiryType",  required = true, 	defaultValue = "") String inquiryType,
			@RequestParam(name = "sabangNo",	 required = false,	defaultValue = "") String sabangNo,
			@RequestParam(name = "recvAddr",	 required = false,	defaultValue = "") String recvAddr,
			@RequestParam(name = "recvAddrDtl`", required = false,	defaultValue = "") String recvAddrDtl) throws IOException	
	{
		int rstl 	  = 0;
		ArrayList<String> fileNameList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		try {
			//2. 첨부파일 등록		
			
			//3. msg 내용 만들기
			sb.append("[배송지변경]");
			sb.append("변경주소:");
			sb.append(recvAddr+" ");
			sb.append(recvAddrDtl);
			sb.append("요청사항: "+content);
			
			Long boardSeq = seqService.getSeq();
			//1. 게시글 등록
			regBoard(boardSeq,sabangNo, inquiryType, "", sb.toString(), ordName, ordPhone);				
			
			//4. CRM 문자 전송
			callCRMApi(boardSeq, sabangNo, inquiryType, "", sb.toString(), ordName, ordPhone, fileNameList);

			rstl = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}	
	
	//배송지 희망일 요청
	@PostMapping("changeDateAjax")
	@ResponseBody
	public int changeDateAjax(		
			@RequestParam(name = "content",      required = true, 	defaultValue = "") String content, 
			@RequestParam(name = "ordName",  required = true, 	defaultValue = "") String ordName,
			@RequestParam(name = "ordPhone",required = true, 	defaultValue = "") String ordPhone, 
			@RequestParam(name = "inquiryType",  required = true, 	defaultValue = "") String inquiryType,
			@RequestParam(name = "sabangNo",	 required = false,	defaultValue = "") String sabangNo,
			@RequestParam(name = "datepicker",	 required = false,	defaultValue = "") String datepicker) throws IOException	
	{
		int rstl 	  = 0;
		ArrayList<String> fileNameList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		try {
			//2. 첨부파일 등록		
			
			//3. msg 내용 만들기
			sb.append("[배송희망일요청]");
			sb.append("배송희망일자:" + datepicker + " ");
			sb.append("요청사항: " + content);
			
			Long boardSeq = seqService.getSeq();
			//1. 게시글 등록
			regBoard(boardSeq,sabangNo, inquiryType, "", sb.toString(), ordName, ordPhone);
				

			
			//4. CRM 문자 전송
			callCRMApi(boardSeq, sabangNo, inquiryType, "", sb.toString(), ordName, ordPhone, fileNameList);

			rstl = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}
	//게시글 등록
	public void regBoard(	
			@RequestParam(name= "boardSeq",	required = true,	defaultValue = "")	Long boardSeq,
			@RequestParam(name= "sabangNo",		required = false,	defaultValue = "")	String sabangNo,
			@RequestParam(name= "inquiryType",	required = false,	defaultValue = "")	String inquiryType,
			@RequestParam(name= "title",		required = false,	defaultValue = "")	String title,
			@RequestParam(name= "content",		required = false,	defaultValue = "") 	String content,			
			@RequestParam(name= "ordName",	required = true,	defaultValue = "")	String ordName,
			@RequestParam(name= "ordPhone",required = true,	defaultValue = "")	String ordPhone) 
		{
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			
			Board board = Board.builder()
					.boardSeq(boardSeq)
					.sabangNo(sabangNo)
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
				
		        String jsonString1 = objectMapper.writeValueAsString(board);
		        
				ExceptionLog ex = ExceptionLog.builder()
									.partner(getPartner())
									.exceptionMessage("Board Regist Fail")
									.exceptionStackTrace(jsonString1)
									.build();
				logService.InsExceptionLog(ex);
				
				EmailMessage emailMessage = EmailMessage.builder()
		                .to("eke6767@naver.com")
		                .subject("게시글 등록 실패")
		                .build();
		        emailService.sendBoardMail(emailMessage, "mail/boardmail", board);
			}
		}catch (Exception e) {
			logger.error("BaseController -> regBoard -> "+ExceptionUtils.getPrintStackTrace(e));
		}
	}
	
	//첨부파일 등록
	public ArrayList<String> regAttach(
			@RequestParam(name= "boardSeq",	required = true,	defaultValue = "")	Long boardSeq,
			@RequestParam(name= "sabangNo",	required = false,	defaultValue = "")	String sabangNo,
			@RequestParam(name = "file",	required = false) 	List<MultipartFile> multipartFileList) 
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
								.sabangNo(sabangNo)
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
			logger.error("BaseController -> regAttach -> "+ExceptionUtils.getPrintStackTrace(e));
		}
		return fileNameList;
	}
	
	//CRM API 호출
	//이름 넣어야함 쇼핑몰 주문번호도넣어야함
	public void callCRMApi(
			@RequestParam(name= "boardSeq",	required = true,	defaultValue = "")	Long boardSeq,
			@RequestParam(name= "sabangNo",		required = false,	defaultValue = "")	String sabangNo,
			@RequestParam(name= "inquiryType",	required = false,	defaultValue = "")	String inquiryType,
			@RequestParam(name= "title",		required = false,	defaultValue = "")	String title,
			@RequestParam(name= "content",		required = false,	defaultValue = "") 	String content,			
			@RequestParam(name= "ordName",	required = true,	defaultValue = "")	String ordName,
			@RequestParam(name= "ordPhone",required = true,	defaultValue = "")	String ordPhone,
			@RequestParam(name= "fileNameList", required = true,	defaultValue = "")	ArrayList<String> fileNameList)
	{
		
		try {
			ObjectMapper 	objectMapper   = new ObjectMapper();
			Partner 		 Partner 	   = partnerService.getPartnerInfo(getPartner());
			Date 			 currentDate   = new Date();
	        SimpleDateFormat sdf           = new SimpleDateFormat("yyyyMMddHHmmss");	        
	        String           formattedDate = sdf.format(currentDate);

	        CRMApiRequest crmApiRequest = CRMApiRequest.builder()
	        						.partner(getPartner())
	        						.comid(Partner.getCommid())
	        						.keycode(Partner.getKeycode())
	        						.hp(ordPhone)
	        						.title(title)
	        						.msg(content)
	        						.proctime(formattedDate)
	        						.inquiryType(inquiryType)
	        						.seq(String.valueOf(boardSeq))
	        						.fileNameList(fileNameList)
	        						.build();
	        	        
	        //api 호출
			ResponseEntity<String> apiResponse = contactService.RegMsg(crmApiRequest);
			
			//결과 업데이트
	        CRMApiResponse response   = objectMapper.readValue(apiResponse.getBody(), CRMApiResponse.class);
	        String 		   jsonString = objectMapper.writeValueAsString(response);	
	        
	        //성공 시
	        if(response.getState().equals("success") && apiResponse.getStatusCode().is2xxSuccessful()) {
	        	ApiLog Response = ApiLog.builder()
		        				.partner(getPartner())
		        				.errorMessage("")
		        				.success(true)
		        				.response(jsonString)
		        				.apiLogSeq(Long.parseLong(crmApiRequest.getSeq()))
		        				.build();
	        	logService.UpdApiLog(Response);
	        }else {
	        	ApiLog Response = ApiLog.builder()
		        				.partner(getPartner())
		        				.errorMessage("")
		        				.success(false)
		        				.response(jsonString)
		        				.apiLogSeq(Long.parseLong(crmApiRequest.getSeq()))
		        				.build();
	        	logService.UpdApiLog(Response);
	        	
	        	//실패시에만 메일 발송
				EmailMessage emailMessage = EmailMessage.builder()
		                .to("eke6767@naver.com")
		                .subject("CRM API 요청 실패")
		                .build();

		        emailService.sendCrmMail(emailMessage, "mail/crmmail", crmApiRequest, response);
	        }	        
		}catch (Exception e) {
			logger.error("BaseController -> callCRMApi -> "+ExceptionUtils.getPrintStackTrace(e));
		}
	}
}
