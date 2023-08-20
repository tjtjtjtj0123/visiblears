package com.xcally.ars.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.OrderLog;
import com.xcally.ars.service.LogService;
import com.xcally.ars.service.OrderService;
import com.xcally.ars.service.PartnerService;
import com.xcally.ars.service.SeqService;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@RestController
public class XmlController {
	
	@Autowired
	private OrderService orderservice;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private PartnerService partnerservice;
	
	@Value("${api.sabangurl}")
    private String sabangUrl;
	
	@Autowired
    private HttpServletRequest request;
	
	@Autowired
	private SeqService seqService;
	
    @GetMapping(value = "/getxml/englander", produces = MediaType.APPLICATION_XML_VALUE)
    public String getXmlData() {
    	LocalDate currentDate = LocalDate.now();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowDay = currentDate.format(formatter);

        LocalDate previousDay = currentDate.minusDays(4);
        String yesterDay = previousDay.format(formatter);
        
        String xmlData = 
        		"<?xml version=\"1.0\" encoding=\"EUC-KR\"?>\r\n"
        		+ "<SABANG_ORDER_LIST>\r\n"
        		+ "	<HEADER>\r\n"
        		+ "		<SEND_COMPAYNY_ID>cs0033</SEND_COMPAYNY_ID>\r\n"
        		+ "		<SEND_AUTH_KEY>r91B548VZyFK3S282SVuX7WEZT92FP5A7A5</SEND_AUTH_KEY>\r\n"
        		+ "		<SEND_DATE>"+nowDay+"</SEND_DATE>\r\n"
        		+ "	</HEADER>\r\n"
        		+ "	<DATA>\r\n"
        		+ "		<ORD_ST_DATE>"+yesterDay+"</ORD_ST_DATE>\r\n"
        		+ "		<ORD_ED_DATE>"+yesterDay+"</ORD_ED_DATE>\r\n"
        		+ "		<ORD_FIELD><![CDATA[ORDER_DATE|IDX|ORDER_ID|PAY_COST|SALE_CNT|PRODUCT_NAME|SKU_VALUE|USER_NAME|USER_TEL|USER_CEL|USER_EMAIL|RECEIVE_NAME|RECEIVE_TEL|RECEIVE_CEL|RECEIVE_ADDR|MALL_ID|ord_field2]]></ORD_FIELD>\r\n"
        		+ "	</DATA>\r\n"
        		+ "</SABANG_ORDER_LIST>";
        
        return xmlData;
    }
    
    @GetMapping(value="/sendxml")
    public void xmlSend(@RequestParam(name= "partner",required = false,defaultValue = "0") String  partner) {
    	
    	if(partner.equals("0"))
    	{
    		return;
    	}

		//사용기한 제크
    	boolean useDtFlag = false;
		useDtFlag = partnerservice.findPartnerUseDt(partner);
		if(!useDtFlag) {
			return;
		}   	
    	
		//url세팅
        String apiUrl = sabangUrl 
        				+ "?xml_url="
        				+ request.getRequestURL().substring(0, request.getRequestURL().indexOf("/", 8))
        				+ "/getxml/" +partner;
        
        //오늘 날짜
        LocalDate 		  currentDate = LocalDate.now();
    	DateTimeFormatter formatter   = DateTimeFormatter.ofPattern("yyyyMMdd");
        String 			  nowDay      = currentDate.format(formatter);
        
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost 	  = new HttpPost(apiUrl);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity 	  = response.getEntity();

            if (entity != null) {
            	try (InputStream inputStream = entity.getContent(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "EUC-KR"))) 
            	{
            		    SAXBuilder saxBuilder   = new SAXBuilder();
            		    Document document 	    = saxBuilder.build(inputStream);
            		    Element sabangOrderList = document.getRootElement();
            		    Element header 		    = sabangOrderList.getChild("HEADER");            		    
            		    String totalCount 	    = header.getChildText("TOTAL_COUNT");

            		    int sucCnt 				= 0;

            		    List<Element> dataList = sabangOrderList.getChildren("DATA");
            		    for (Element data : dataList) {
            		    	Long orderSeq = seqService.getSeq();
            	            Order order = Order.builder()
            	            		.seqNo(orderSeq)
    	            				.partner(partner)
    	            				.sabangNo(data.getChildText("IDX"))
    	            				.shopNo(data.getChildText("ORDER_ID"))
    	            				.payAmt(Integer.parseInt(data.getChildText("PAY_COST")))        				
    	            				.quantity(Integer.parseInt(data.getChildText("SALE_CNT")))
    	            				.productName(data.getChildText("PRODUCT_NAME"))
    	            				.ordererName(data.getChildText("USER_NAME"))
    	            				.ordererPhone1(data.getChildText("USER_TEL").replaceAll("-", ""))
    	            				.ordererPhone2(data.getChildText("USER_CEL").replaceAll("-", ""))
    	            				.receiverName(data.getChildText("RECEIVE_NAME"))
    	            				.receiverPhone1(data.getChildText("RECEIVE_TEL").replaceAll("-", ""))
    	            				.receiverPhone2(data.getChildText("RECEIVE_TEL").replaceAll("-", ""))
    	            				.receiverAddress(data.getChildText("RECEIVE_ADDR"))	
    	            				.ordererEmail(data.getChildText("USER_EMAIL"))
    	            				.mallOrderDt(data.getChildText("MALL_ID")+" "+data.getChildText("ORDER_DATE"))
    	            				.mall(data.getChildText("MALL_ID"))
    	            				.orderDt(data.getChildText("ORDER_DATE"))
    	            				.ordField2(data.getChildText("ord_field2"))
    	            				.optionName(data.getChildText("SKU_VALUE"))
    	            				.build();
            	            
            	            int rstl = orderservice.InsOrder(order);
            	            sucCnt+=rstl;
        		    }
            		    
        		    OrderLog orderLog = OrderLog.builder()
        		    					.partner(partner)
        		    					.regYmd(nowDay)
        		    					.targetCnt(totalCount)
        		    					.sucCnt(Integer.toString(sucCnt))
        		    					.build();
        		    		
        		    int rstl = logService.InsOrderLog(orderLog);
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
