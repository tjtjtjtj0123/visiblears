package com.xcally.ars.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.OrderLog;
import com.xcally.ars.service.OrderLogService;
import com.xcally.ars.service.OrderService;

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
	private OrderLogService orderLogService;
	
	@Value("${api.sabangurl}")
    private String sabangUrl;
	
	@Autowired
    private HttpServletRequest request;
	
    @GetMapping(value = "/getxml/englander", produces = MediaType.APPLICATION_XML_VALUE)
    public String getXmlData() {
    	LocalDate currentDate = LocalDate.now();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowDay = currentDate.format(formatter);

        LocalDate previousDay = currentDate.minusDays(1);
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
        		+ "		<ORD_FIELD><![CDATA[ORDER_DATE|IDX|ORDER_ID|PAY_COST|SALE_CNT|PRODUCT_NAME|USER_NAME|USER_TEL|USER_CEL|USER_EMAIL|RECEIVE_NAME|RECEIVE_TEL|RECEIVE_CEL|RECEIVE_ADDR|MALL_ID]]></ORD_FIELD>\r\n"
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
    	
        String apiUrl = sabangUrl 
        				+ "?xml_url="
        				+ request.getRequestURL().substring(0, request.getRequestURL().indexOf("/", 8))
        				+ "/getxml/" +partner;
        

        LocalDate currentDate = LocalDate.now();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowDay = currentDate.format(formatter);
        
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost 	  = new HttpPost(apiUrl);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity 	  = response.getEntity();

            if (entity != null) {
            	try (InputStream inputStream = entity.getContent(); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "EUC-KR"))) 
            	{
            		    SAXBuilder saxBuilder = new SAXBuilder();
            		    Document document = saxBuilder.build(inputStream);
            		    
            		    Element sabangOrderList = document.getRootElement();

            		    // Access HEADER
            		    Element header 		 = sabangOrderList.getChild("HEADER");
            		    String sendCompanyID = header.getChildText("SEND_COMPAYNY_ID");
            		    String sendDate 	 = header.getChildText("SEND_DATE");
            		    String totalCount 	 = header.getChildText("TOTAL_COUNT");

            		    System.out.println("SEND_COMPANY_ID: " + sendCompanyID);
            		    System.out.println("SEND_DATE: " + sendDate);
            		    System.out.println("TOTAL_COUNT: " + totalCount);
            		    int sucCnt = 0;
            		    // Access DATA
            		    List<Element> dataList = sabangOrderList.getChildren("DATA");
            		    for (Element data : dataList) {
            		        String orderDate 	= data.getChildText("ORDER_DATE");
            		        String idx 			= data.getChildText("IDX");
            		        String orderId 		= data.getChildText("ORDER_ID");
            		        String payCost 		= data.getChildText("PAY_COST");
            		        String saleCnt 		= data.getChildText("SALE_CNT");
            		        String productName 	= data.getChildText("PRODUCT_NAME");
            		        String userName 	= data.getChildText("USER_NAME");
            		        String userTel 		= data.getChildText("USER_TEL");
            		        String userCel 		= data.getChildText("USER_CEL");
            		        String userEmail 	= data.getChildText("USER_EMAIL");
            		        String receiveName 	= data.getChildText("RECEIVE_NAME");
            		        String receiveTel 	= data.getChildText("RECEIVE_TEL");
            		        String receiveCel 	= data.getChildText("RECEIVE_CEL");
            		        String receiveAddr 	= data.getChildText("RECEIVE_ADDR");
            		        String mallId		= data.getChildText("MALL_ID");
            		        
            		        

            	            Order order = Order.builder()
    	            				.partner(partner)
    	            				.sabangNo(idx)
    	            				.shopNo(orderId)
    	            				.payAmt(Integer.parseInt(payCost))        				
    	            				.quantity(Integer.parseInt(saleCnt))
    	            				.productName(productName)
    	            				.ordererName(userName)
    	            				.ordererPhone1(userTel)
    	            				.ordererPhone2(userCel)
    	            				
    	            				.receiverName(receiveName)
    	            				.receiverPhone1(receiveTel)
    	            				.receiverPhone2(receiveCel)
    	            				.receiverAddress(receiveAddr)
    	            				
    	            				.ordererEmail(userEmail)
    	            				.mallOrderDt(mallId+" "+orderDate)
    	            				.mall(mallId)
    	            				.orderDt(orderDate)
    	            				.build();
            	            int rstl = orderservice.InsOrder(order);
            	            sucCnt+=rstl;
		            		        // ... print other elements within DATA
		    		        // ... access other elements within DATA\
		    	            /*
		    		        System.out.println("============================");
		    		        System.out.println("ORDER_DATE: "	+ orderDate);mk
		    		        System.out.println("MALL_ID: "		+ mallId);
		    		        System.out.println("IDX: "   	 	+ idx);
		    		        System.out.println("ORDER_ID: " 	+ orderId);
		    		        System.out.println("PAY_COST: " 	+ payCost);
		    		        System.out.println("SALE_CNT: " 	+ saleCnt);w
		    		        System.out.println("PRODUCT_NAME: " + productName);
		    		        System.out.println("USER_NAME: " 	+ userName);
		    		        System.out.println("USER_TEL: " 	+ userTel);
		    		        System.out.println("USER_CEL: " 	+ userCel);
		    		        System.out.println("USER_EMAIL: " 	+ userEmail);
		    		        System.out.println("RECEIVE_NAME: " + receiveName);
		    		        System.out.println("RECEIVE_TEL: " 	+ receiveTel);
		    		        System.out.println("RECEIVE_CEL: " 	+ receiveCel);
		    		        System.out.println("RECEIVE_ADDR: " + receiveAddr);
		    		        System.out.println("============================");
		    		        */
		    		        
        		    }
        		    System.out.println("sucCnt:"+sucCnt);
        		    OrderLog orderLog = OrderLog.builder()
        		    					.partner(partner)
        		    					.regYmd(nowDay)
        		    					.targetCnt(totalCount)
        		    					.sucCnt(Integer.toString(sucCnt))
        		    					.build();
        		    		
        		    int rstl = orderLogService.InsOrderLog(orderLog);
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
