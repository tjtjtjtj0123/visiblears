package com.xcally.ars.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.popbill.api.MessageService;
import com.xcally.ars.domain.Commercekorea;
import com.xcally.ars.domain.EmailMessage;
import com.xcally.ars.domain.ExcelSampleData;
import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.common.EncryptionService;
import com.xcally.ars.domain.common.NaverShortenUrlApi;
import com.xcally.ars.domain.crm.CRMApiCusRequest;
import com.xcally.ars.domain.crm.CRMApiMsgRequest;
import com.xcally.ars.service.CrmServiceImpl;
import com.xcally.ars.service.EmailService;
import com.xcally.ars.service.OrderService;
import com.xcally.ars.service.SeqService;

import org.apache.commons.io.FilenameUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
public class testController {

	@Autowired
	private OrderService orderservice;
	
	@Autowired
	private EncryptionService encryptionService;
	
	@Autowired
	private NaverShortenUrlApi naverShortenUrlApi;
	
	@Autowired
	private SeqService seqService;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private CrmServiceImpl crmServiceImpl;
	
	@Value("${naver.shortenUrl.client.id}")
    private String YOUR_CLIENT_ID;
	
	@Value("${naver.shortenUrl.client.secret}")
    private String YOUR_CLIENT_SECRET;
	
	@Autowired
    private MessageService messageService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("index")
	public String index(Model model) {
		EmailMessage emailMessage = EmailMessage.builder()
                .to("eke6767@naver.com")
                .subject("테스트제목")
                .build();

        //emailService.sendBoardMail(emailMessage, "email");
		return "index";
	}

	@PostMapping(value = "get")
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
	
	@GetMapping("/navertest")
	public void naverTest() {
        String clientId = YOUR_CLIENT_ID; //애플리케이션 클라이언트 아이디값"
        String clientSecret = YOUR_CLIENT_SECRET; //애플리케이션 클라이언트 시크릿값"

        String originalURL = "https://developers.naver.com/notice";
        String apiURL = "https://openapi.naver.com/v1/util/shorturl?url=" + originalURL;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = naverShortenUrlApi.get(apiURL,requestHeaders);

        System.out.println(responseBody);
	}
	
	
	@GetMapping("/excel")
	public String main() {
		return "excel";
	}

	@PostMapping("/excel/read")
	public String readExcel(@RequestParam("file") MultipartFile file, Model model) throws IOException {

		// 컬럼 array
		String[] columnArr = { "등록일자", "주문번호", "주문 상품명", "수량", "주문자명", "주문자연락처1", "주문자연락처2", "주문자이메일주소", "수취인명",
				"수취인연락처1", "수취인연락처2", "수취인 주소", "결제", "사방넷주문번호" };

		// 컬럼 index array
		int[] indexArr = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

		List<ExcelSampleData> dataList = new ArrayList<>();

		// 확장자 검사
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

		// 컬럼인덱스 찾기
		Sheet worksheet = workbook.getSheetAt(0);

		for (int i = 0; i < worksheet.getRow(0).getLastCellNum(); i++) {
			for (int j = 0; j < columnArr.length; j++) {
				if (columnArr[j].equals(worksheet.getRow(0).getCell(i).toString())) {
					indexArr[j] = i;
				}
			}
		}

		String[] columnArr2 = { "등록일자", "주문번호", "주문 상품명", "수량", "주문자명", "주문자연락처1", "주문자연락처2", "주문자이메일주소", "수취인명",
				"수취인연락처1", "수취인연락처2", "수취인 주소", "결제", "사방넷주문번호" };

		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

			Row row = worksheet.getRow(i);

			Order order = Order.builder().partner("sornia")
					.sabangNo(String.valueOf((int) row.getCell(indexArr[13]).getNumericCellValue()))
					.shopNo(row.getCell(indexArr[1]).getStringCellValue())
					.payAmt((int) row.getCell(indexArr[12]).getNumericCellValue())

					.quantity((int) row.getCell(indexArr[3]).getNumericCellValue())
					.productName(row.getCell(indexArr[2]).getStringCellValue())
					.ordererName(row.getCell(indexArr[4]).getStringCellValue())
					.ordererPhone1(row.getCell(indexArr[5]).getStringCellValue())
					.ordererPhone2(row.getCell(indexArr[6]).getStringCellValue())

					.receiverName(row.getCell(indexArr[8]).getStringCellValue())
					.receiverPhone1(row.getCell(indexArr[9]).getStringCellValue())
					.receiverPhone2(row.getCell(indexArr[10]).getStringCellValue())
					.receiverAddress(row.getCell(indexArr[11]).getStringCellValue())

					.ordererEmail(row.getCell(indexArr[7]).getStringCellValue()).build();
			// row.getCell(indexArr[0]).getStringCellValue()
			int rstl = orderservice.InsOrder(order);
		}

		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

			Row row = worksheet.getRow(i);

			ExcelSampleData data = new ExcelSampleData();

			data.setNum((int) row.getCell(0).getNumericCellValue()); // 실수
			data.setName(row.getCell(1).getStringCellValue()); // 문자열
			data.setEmail(row.getCell(2).getStringCellValue()); // 논리

			dataList.add(data);
		}

		model.addAttribute("datas", dataList);

		return "excelList";
	}
	@GetMapping("/encTest")
	public void encTest() throws Exception {
		
		String originalData = "010-8007-7296";

		String encryptedData = encryptionService.encrypt(originalData);
		String decryptedData = encryptionService.decrypt(encryptedData);


		logger.error("Original Data: " + originalData);
		logger.error("Encrypted Data: " + encryptedData);
		logger.error("Decrypted Data: " + decryptedData);
        String base64EncodedString = encryptedData;
        byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);
        int decodedLength = decodedBytes.length;

        // 마진 추가
        int varcharLength = decodedLength + 20;

        logger.error("VARCHAR 컬럼 길이: " + varcharLength);
		return;		
	}
	
	@GetMapping("/custest")
	public void custest() {
		
		CRMApiCusRequest asd = CRMApiCusRequest.builder()
						.comid("xcally")
						.keycode("0bs6h0h3jybk75cv7xwxq7oxg6a5x9uh")
						.hp("01080077293")
						.name("김단태")
						.memo("여기에주문정보?")
						.zipcode("12354")
						.address("창동")
						.seq(seqService.getSeq().toString())
						.partner("englander")
						.build();
						
		//ResponseEntity<String> dd= crmServiceImpl.RegCus(asd);
		
	}
	@GetMapping("/CumusKoera")
	public void CumusKoera() {
		String apiUrl = "https://service.pluscl.com/open/base_data";
        String authKey = "E8EBEBA8-8E54-432A-97FE-B31366985EE6";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("auth_key", authKey);

        String requestBody = "{ \"company_id\": \"2315\", \"job_type\": \"search\", \"type\": \"warehouse_type\", \"data\": \"\" }";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Commercekorea> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                Commercekorea.class
        );

        Commercekorea apiResponse = responseEntity.getBody();
        
        // 여기서 ApiResponse 객체를 사용하여 필요한 작업 수행
        System.out.println("API 호출 결과: " + apiResponse.toString());
	}
}
