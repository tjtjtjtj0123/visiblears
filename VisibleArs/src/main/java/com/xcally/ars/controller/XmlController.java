package com.xcally.ars.controller;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
@RestController
public class XmlController {
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String getXmlData() {
        String xmlData = 
        		"<?xml version=\"1.0\" encoding=\"EUC-KR\"?>\r\n"
        		+ "<SABANG_ORDER_LIST>\r\n"
        		+ "	<HEADER>\r\n"
        		+ "		<SEND_COMPAYNY_ID>OOOO</SEND_COMPAYNY_ID>\r\n"
        		+ "		<SEND_AUTH_KEY>XXXXXXXXXXXXXXXXXXXXXX</SEND_AUTH_KEY>\r\n"
        		+ "		<SEND_DATE>20190729</SEND_DATE>\r\n"
        		+ "	</HEADER>\r\n"
        		+ "	<DATA>\r\n"
        		+ "		<ORD_ST_DATE>20190129</ORD_ST_DATE>\r\n"
        		+ "		<ORD_ED_DATE>20190729</ORD_ED_DATE>\r\n"
        		+ "		<ORD_FIELD><![CDATA[IDX|ORDER_ID|MALL_ID|ORDER_STATUS|DELV_MSG|RECEIVE_TEL|RECEIVE_CEL|RECEIVE_NAME|RECEIVE_ZIPCODE|RECEIVE_ADDR|P_PRODUCT_NAME|P_SKU_VALUE|P_EA]]></ORD_FIELD>\r\n"
        		+ "		<ORDER_STATUS>004</ORDER_STATUS>\r\n"
        		+ "	</DATA>\r\n"
        		+ "</SABANG_ORDER_LIST>";
        
        return xmlData;
    }
    
    @GetMapping(value="/asd")
    public void xmlSend() {
        String apiUrl = "https://sbadmin12.sabangnet.co.kr/RTL_API/xml_order_info.html?xml_url="+"http://localhost:8080/xml";


        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(apiUrl);
            //httpPost.setEntity(new StringEntity(requestBody, "EUC-KR"));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream inputStream = entity.getContent();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "EUC-KR"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // API 응답 데이터 처리
                        System.out.println(line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
