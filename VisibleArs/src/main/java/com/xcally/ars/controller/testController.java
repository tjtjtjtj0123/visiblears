package com.xcally.ars.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xcally.ars.domain.ExcelSampleData;
import com.xcally.ars.domain.Order;
import com.xcally.ars.service.OrderService;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Controller
@RequestMapping("/")
public class testController {

	@Autowired
	private OrderService orderservice;
	
	@GetMapping("index")
	public String index(Model model) {

		return "index";
	}
	
	@PostMapping(value="get")
	@ResponseBody
	public Order getOrder(Order order) {
		System.out.println(order);
		Order rstl = null;
        
    	try {
    	   
            rstl = orderservice.getOrder(order);

            System.out.println(order.getRegDt());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return rstl;
	}
	
	@GetMapping("/excel")
    public String main() {
        return "excel";
    }
	  
	  
    @PostMapping("/excel/read")
    public String readExcel(@RequestParam("file") MultipartFile file, Model model)
            throws IOException {
    	
    	//컬럼 array
    	String [] columnArr = {"등록일자", "주문번호", "주문 상품명", "수량", "주문자명", 
    						   "주문자연락처1", "주문자연락처2", "주문자이메일주소", "수취인명", "수취인연락처1", 
    						   "수취인연락처2", "수취인 주소", "결제","사방넷주문번호"};
    	
    	//컬럼 index array
    	int [] indexArr= {-1,-1,-1,-1,-1,
    			          -1,-1,-1,-1,-1,
    			          -1,-1,-1,-1};
    	
        List<ExcelSampleData> dataList = new ArrayList<>();

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
        
        for(int i=0; i<worksheet.getRow(0).getLastCellNum(); i++) {
        	for(int j=0;j<columnArr.length;j++) {
        		if(columnArr[j].equals(worksheet.getRow(0).getCell(i).toString())) {
        			indexArr[j] = i;
        		}
        	}
        }        
    	
        String [] columnArr2 = {"등록일자", "주문번호", "주문 상품명", "수량", "주문자명", 
				   "주문자연락처1", "주문자연락처2", "주문자이메일주소", "수취인명", "수취인연락처1", 
				   "수취인연락처2", "수취인 주소", "결제","사방넷주문번호"};
    	
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

            Row row = worksheet.getRow(i);         
            
            Order order = Order.builder()
            				.partner("sornia")
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
            				.build();
            //row.getCell(indexArr[0]).getStringCellValue()
            int rstl = orderservice.InsOrder(order);
        }

        
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

            Row row = worksheet.getRow(i);

            ExcelSampleData data = new ExcelSampleData();

            data.setNum((int) row.getCell(0).getNumericCellValue());    // 실수
            data.setName(row.getCell(1).getStringCellValue());          // 문자열
            data.setEmail(row.getCell(2).getStringCellValue());         // 논리

            dataList.add(data);
        }

        model.addAttribute("datas", dataList);

        return "excelList";
    }
}
