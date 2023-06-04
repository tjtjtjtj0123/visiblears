package com.xcally.ars.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xcally.ars.domain.ExcelSampleData;
import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.Partner;
import com.xcally.ars.domain.common.SimpleResult;
import com.xcally.ars.service.OrderService;
import com.xcally.ars.service.PartnerService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/partner")
public class PartnerController {

	@Autowired
	private PartnerService partnerservice;
	
	@Autowired
	private OrderService orderservice;
	
	@GetMapping("/login")
	public String partnerLogin() {
		return "parnerlogin";
	}
	
	@PostMapping("/chk")
	@ResponseBody
	public boolean Chkpartner(Partner partner, HttpSession session, HttpServletResponse response) {
		Partner objPartner=null;
		String SessionID = null;
		boolean rstl=false;
		try {
			objPartner = partnerservice.findPartner(partner);
			rstl = objPartner==null?false:true;
			if(rstl) {			
				System.out.println(partner.getPartnerId());
				
				//세션 설정
				SessionID = UUID.randomUUID().toString();
				session.setAttribute(SessionID ,partner.getPartnerId());
				
				//쿠키 설정
		        Cookie sessionCookie = new Cookie("xcallyadmsession", SessionID);
		        sessionCookie.setMaxAge(60*60*24);
		        sessionCookie.setPath("/partner");
		        response.addCookie(sessionCookie);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(rstl);
		return rstl;
	}
	
	//엑셀 업로드 페이지
	@GetMapping("/excel")
    public String main(@CookieValue(name ="xcallyadmsession",required = false)String xcallyadmsession, HttpSession session,HttpServletResponse response,Model model) {	
		String partner_id = null;
		boolean useDtFlag = false;
		try {
			
			if(xcallyadmsession == null) {
				return "redirect:/partner/login";
			}
			
			partner_id = (String)session.getAttribute(xcallyadmsession);
    		if(partner_id == null) {
		        response.setContentType("text/html; charset=euc-kr");
		        response.setCharacterEncoding("euc-kr");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('로그인되어있지 않습니다.');location.href='/partner/login';</script>");
    		} 
			
			//사용기한 제크
			useDtFlag = partnerservice.findPartnerUseDt(partner_id);
			
			if(!useDtFlag) {
		        response.setContentType("text/html; charset=euc-kr");
		        response.setCharacterEncoding("euc-kr");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('사용 기한이 지났습니다.');location.href='/partner/login';</script>");
				return "partnerexcel";
			}
			
			if(partner_id == null) {
				return "redirect:/partner/login";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("partnerId", partner_id);
        return "partnerexcel";
    }

	@ResponseBody
    @PostMapping("/excel/read")
    public SimpleResult readExcel(@RequestParam("file") MultipartFile file, Model model, @CookieValue(name ="xcallyadmsession",required = false)String xcallyadmsession, HttpSession session)
            throws IOException {

    	String partner_id = null;
    	Partner objPartner = null;
    	boolean useDtFlag = false;
    	SimpleResult simpleResult = new SimpleResult();
    	int tmpSabangNo = 0;
    	try {
    		if(xcallyadmsession == null) {
    			simpleResult.setErrCode(999);
    			simpleResult.setErrMsg("session null");
    		}
    		
    		partner_id = (String)session.getAttribute(xcallyadmsession);
    		
    		if(partner_id == null) {
    			simpleResult.setErrCode(998);
    			simpleResult.setErrMsg("session null");
    		} 
    		else {   	        
    			//사용기한 제크

    			useDtFlag = partnerservice.findPartnerUseDt(partner_id);
    			
    			if(!useDtFlag) {
        			simpleResult.setErrCode(997);
        			simpleResult.setErrMsg("사용일자가 지났습니다.");
        			return simpleResult;
    			}
    			
    	        //확장자 검사
    	        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

    	        if (!extension.equals("xlsx") && !extension.equals("xls")) {
    	            throw new IOException("엑셀파일만 업로드 해주세요.");
    	        }

    	        Workbook workbook = null;

    	        if (extension.equals("xlsx")) {
    	            workbook = new XSSFWorkbook(file.getInputStream());
    	        } else if (extension.equals("xls")) {
    	            workbook = new HSSFWorkbook(file.getInputStream());
    	        }

    	        //컬럼인덱스 찾기
    	        Sheet worksheet = workbook.getSheetAt(0);
    	        
    	    	
    	    	//컬럼 array
    	    	String [] columnArr = {"등록일자", "주문번호", "주문 상품명", "수량", "주문자명", 
    	    						   "주문자연락처1", "주문자연락처2", "주문자이메일주소", "수취인명", "수취인연락처1", 
    	    						   "수취인연락처2", "수취인 주소", "결제","사방넷주문번호", "주문일자"};
    	    	
    	    	//컬럼 index array
    	    	int [] indexArr= {-1,-1,-1,-1,-1,
    	    			          -1,-1,-1,-1,-1,
    	    			          -1,-1,-1,-1,-1};

    	        for(int i=0; i<worksheet.getRow(0).getLastCellNum(); i++) {
    	        	for(int j=0;j<columnArr.length;j++) {
    	        		if(columnArr[j].equals(worksheet.getRow(0).getCell(i).toString())) {
    	        			indexArr[j] = i;
    	        		}
    	        	}
    	        }        

    	    	
    	        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

    	            Row row = worksheet.getRow(i);         
    	            
    	            Order order = Order.builder()
    	            				.partner(partner_id)
    	            				.sabangNo(String.valueOf((int)row.getCell(indexArr[13]).getNumericCellValue()))
    	            				.shopNo(row.getCell(indexArr[1]).getStringCellValue())
    	            				.payAmt((int) row.getCell(indexArr[12]).getNumericCellValue())
    	            				
    	            				.quantity((int)row.getCell(indexArr[3]).getNumericCellValue())
    	            				.productName(row.getCell(indexArr[2]).getStringCellValue())
    	            				.ordererName(row.getCell(indexArr[4]).getStringCellValue())
    	            				.ordererPhone1(row.getCell(indexArr[5]).getStringCellValue())
    	            				.ordererPhone2(row.getCell(indexArr[6]).getStringCellValue())
    	            				
    	            				.receiverName(row.getCell(indexArr[8]).getStringCellValue())
    	            				.receiverPhone1(row.getCell(indexArr[9]).getStringCellValue())
    	            				.receiverPhone2(row.getCell(indexArr[10]).getStringCellValue())
    	            				.receiverAddress(row.getCell(indexArr[11]).getStringCellValue())
    	            				
    	            				.ordererEmail(row.getCell(indexArr[7]).getStringCellValue())
    	            				.mallOrderDt(row.getCell(indexArr[14]).getStringCellValue())
    	            				.mall(row.getCell(indexArr[14]).getStringCellValue().substring(0,row.getCell(indexArr[14]).getStringCellValue().length()-15))
    	            				.orderDt(row.getCell(indexArr[14]).getStringCellValue().substring(row.getCell(indexArr[14]).getStringCellValue().length()-14, row.getCell(indexArr[14]).getStringCellValue().length()))
    	            				.build();
    	            orderservice.InsOrder(order);
    	            
    	            tmpSabangNo = Math.max(tmpSabangNo, Integer.parseInt(order.getSabangNo()));
    	        }
    	        
    	        objPartner = Partner.builder()
    	        			.sabangNo(String.valueOf(tmpSabangNo))
    	        			.partnerId(partner_id)
    	        			.build();
    	        
    	        boolean rstl = partnerservice.updateSabangNo(objPartner);
    	        System.out.println(rstl);
    	        
    	        simpleResult.setErrCode(0);
    	        simpleResult.setErrMsg("OK");    		
    	        }    		
    	}
    	catch (Exception e) {
			e.printStackTrace();
	        simpleResult.setErrCode(888);
	        simpleResult.setErrMsg("Exception");  
		}

        return simpleResult;
    }
}
