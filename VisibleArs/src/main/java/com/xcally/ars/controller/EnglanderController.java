package com.xcally.ars.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xcally.ars.domain.Attach;
import com.xcally.ars.domain.Board;
import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.common.S3Uploader;
import com.xcally.ars.service.AttachService;
import com.xcally.ars.service.BoardService;
import com.xcally.ars.service.ContactService;
import com.xcally.ars.service.OrderService;
import com.xcally.ars.service.PartnerService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
@RequestMapping("/englander")
public class EnglanderController {
	@Autowired
	private OrderService orderservice;

	@Autowired
	private AttachService attachmentService;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private PartnerService partnerService;

	@Autowired
	private ContactService contactService;
	
	private final S3Uploader s3Uploader;

	static String partner = "englander";

	@GetMapping("menu") // 메인메뉴
	public String menu(Model model)
	{
		return partner+"/menu";
	}

	@GetMapping("sub") // 서브메뉴
	public String sub(Model model, String sabangNo) {
		model.addAttribute("sabangNo", sabangNo);
		return partner+"/sub";
	}

	@GetMapping("auth") // 인증페이지
	public String auth(Model model) {
		return partner+"/auth";
	}

	@PostMapping("noorder")//주문정보없음
	public String order(Model model,String ordererName,String ordererPhone1, String receiverAddress) {
		Order order = Order.builder()
				.ordererName(ordererName)
				.ordererPhone1(ordererPhone1)
				.receiverAddress(receiverAddress)
				.build();
	
		model.addAttribute("order",order);
		return partner+"/noorder";
	}
	
	@GetMapping("call") // 기타 문의
	public String call() {		
		return partner+"/call";
	}

	@GetMapping("detail") // 주문상세
	public String detail(Model model, int sabangNo) {

		List<Order> orderlist     = null;
		List<Order> destOrderlist = new ArrayList<Order>();
		HashMap<String, Integer> quantityMap =new HashMap<>();		
		
		try {

			model.addAttribute("sabangNo", sabangNo);
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

		return partner+"/detail";
	}

	@GetMapping("inquiry") // 기타 문의
	public String inquiry(Model model, 
			@RequestParam(name= "sabangNo",required = false,defaultValue = "0") String  sabangNo,
			@RequestParam(name= "ordererName",required = false,defaultValue = "0") String  ordererName,
			@RequestParam(name= "ordererPhone1",required = false,defaultValue = "0") String  ordererPhone1,
			@RequestParam(name= "receiverAddress",required = false,defaultValue = "0") String  receiverAddress) {
		
		if(sabangNo.equals("0")) {		
			if(!ordererName.equals("0")) {
				model.addAttribute("orderer_name", ordererName);
			}
			if(!ordererPhone1.equals("0")) {
				model.addAttribute("orderer_phone1", ordererPhone1);		
			}
			model.addAttribute("order_no_shop", "0");
		}
		else {

			List<Order> orderlist = null;
			orderlist = orderservice.findOrderBySabangNo(Integer.parseInt(sabangNo));
			model.addAttribute("mall_order_dt", orderlist.get(0).getMallOrderDt());
			model.addAttribute("orderer_name", orderlist.get(0).getOrdererName());
			model.addAttribute("orderer_phone1", orderlist.get(0).getOrdererPhone1().replace(("-"), ""));
		}	
		return partner+"/inquiry";		
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
			order.setPartner(partner);
			
			rstl = orderservice.getOrder(order);
			//rstl = orderservice.getOrderDecode(order);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}

	@PostMapping("uploadimglist")
	@ResponseBody
	public int uploadlist(@RequestParam(name = "file",required=false) List<MultipartFile> multipartFileList,String title,String content, String orderer_name,String orderer_phone1, String inquiry_type,
			@RequestParam(name= "mall_order_dt",required = false,defaultValue = "0")String mall_order_dt)throws IOException	{
		int rstl 	  = 0;
		int board_seq = 0;
		Attach attach = null;
		Board board   = null;
		ArrayList<String> fileNameList = new ArrayList<String>();
		String fullContent = "";
		try {

			//게시글 등록
			board = Board.builder()
					.partner(partner)
					.title(title)
					.content(content)
					.inquiryName(orderer_name)
					.inquiryPhone(orderer_phone1)
					.mallOrderDt(mall_order_dt)
					.inquiryType(inquiry_type)
					.build();
			rstl = boardService.WriteBoard(board);			
			
			board_seq = board.getBoardSeq();
			
			//파일 등록
			if(multipartFileList != null ) 
			{
				for (int i = 0; i < multipartFileList.size(); i++) {
					String fileName = s3Uploader.uploadFiles(multipartFileList.get(i), partner);
					
					fileNameList.add(fileName);
					
					attach = Attach.builder()
							.boardSeq(board_seq)
							.s3Addr(fileName)
							.build();
					
					int aa = attachmentService.WriteAttach(attach);
				}
			}
			
			//문자 등록
			Partner Partner = partnerService.getPartnerInfo(partner);
			//날짜 가져오기
			Date currentDate = new Date();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	        
	        String formattedDate = sdf.format(currentDate);
	        
	        //내용 만들기
	        fullContent = title
	        			 + content;
	        
	        if(multipartFileList != null) {
	        	for(String tmp :fileNameList) {	        	
	        		fullContent += tmp + "/n";
	        	}
	        }
	        			
	        
			ResponseEntity<String> userInfo = contactService.Contact(Partner, orderer_phone1, fullContent, formattedDate, Integer.toString(board_seq));
			String responseBody = userInfo.getBody();
			
			//System.out.println(responseBody);

			rstl = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rstl;
	}
}
