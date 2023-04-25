package com.xcally.ars.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.xcally.ars.service.AttachService;
import com.xcally.ars.service.BoardService;
import com.xcally.ars.service.OrderService;
import com.xcally.ars.service.S3Uploader;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
@RequestMapping("/sornia")
public class SorniaController {
	@Autowired
	private OrderService orderservice;

	@Autowired
	private AttachService attachmentService;

	@Autowired
	private BoardService boardService;
	
	private final S3Uploader s3Uploader;

	static String partner = "sornia";

	@GetMapping("menu") // 메인메뉴
	public String menu() {

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

		List<Order> orderlist = null;
		Order order = null;
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
		System.out.println(ordererPhone1);

		
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
			model.addAttribute("order_no_shop", orderlist.get(0).getShopNo());
			model.addAttribute("orderer_name", orderlist.get(0).getOrdererName());
			model.addAttribute("orderer_phone1", orderlist.get(0).getOrdererPhone1());
		}	
		return partner+"/inquiry";		
	}
	
	@PostMapping(value = "get") // 인증페이지 ajax
	@ResponseBody
	public Order getOrder(Order order) {
		Order rstl = null;
		String addr = "";
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
			
			rstl = orderservice.getOrder(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rstl;
	}

	@PostMapping("uploadimglist")
	@ResponseBody
	public int uploadlist(@RequestParam("file") List<MultipartFile> multipartFileList,String title,String content, String orderer_name,String orderer_phone1,
			@RequestParam(name= "order_no_shop",required = false,defaultValue = "0")String order_no_shop)throws IOException 
	{
		int rstl = 0;
		int board_seq = 0;
		Attach attach = null;
		Board board = null;
		
		try {

			board = Board.builder()
					.partner(partner)
					.title(title)
					.content(content)
					.inquiryName(orderer_name)
					.inquiryPhone(orderer_phone1)
					.shopNo(order_no_shop)
					.build();
			rstl = boardService.WriteBoard(board);
			board_seq = board.getBoardSeq();
			for (int i = 0; i < multipartFileList.size(); i++) {
				String fileName = s3Uploader.uploadFiles(multipartFileList.get(i), partner);

				
				attach = Attach.builder()
						.boardSeq(board_seq)
						.s3Addr(fileName)
						.build();
				
				int aa = attachmentService.WriteAttach(attach);
			}
			rstl = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rstl;
	}
}
