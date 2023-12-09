package com.xcally.ars.controller;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
import com.xcally.ars.domain.common.DataResponse;
import com.xcally.ars.domain.common.SimpleResult;
import com.xcally.ars.service.OrderService;
import com.xcally.ars.service.PartnerService;
import com.xcally.ars.service.SeqService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/partner")
public class PartnerController {

	@Autowired
	private PartnerService partnerservice;

	@Autowired
	private OrderService orderservice;

	@Autowired
	private SeqService seqService;

	@GetMapping("/login")
	public String partnerLogin() {
		return "parnerlogin";
	}

	@PostMapping("/chk")
	@ResponseBody
	public boolean Chkpartner(Partner partner, HttpSession session, HttpServletResponse response) {
		Partner objPartner = null;
		String SessionID = null;
		boolean rstl = false;
		try {
			objPartner = partnerservice.findPartner(partner);
			rstl = objPartner == null ? false : true;
			if (rstl) {
				System.out.println(partner.getPartnerId());

				// 세션 설정
				SessionID = UUID.randomUUID().toString();
				session.setAttribute(SessionID, partner.getPartnerId());

				// 쿠키 설정
				Cookie sessionCookie = new Cookie("xcallyadmsession", SessionID);
				sessionCookie.setMaxAge(60 * 60 * 24);
				sessionCookie.setPath("/partner");
				response.addCookie(sessionCookie);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(rstl);
		return rstl;
	}

	// 엑셀 업로드 페이지
	@GetMapping("/menu")
	public String partnermenu(@CookieValue(name = "xcallyadmsession", required = false) String xcallyadmsession,
			HttpSession session, HttpServletResponse response, Model model) {
		String partner_id = null;
		boolean useDtFlag = false;
		try {

			if (xcallyadmsession == null) {
				return "redirect:/partner/login";
			}

			partner_id = (String) session.getAttribute(xcallyadmsession);
			if (partner_id == null) {
				response.setContentType("text/html; charset=euc-kr");
				response.setCharacterEncoding("euc-kr");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('로그인되어있지 않습니다.');location.href='/partner/login';</script>");
			}

			// 사용기한 제크
			useDtFlag = partnerservice.findPartnerUseDt(partner_id);

			if (!useDtFlag) {
				response.setContentType("text/html; charset=euc-kr");
				response.setCharacterEncoding("euc-kr");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('사용 기한이 지났습니다.');location.href='/partner/login';</script>");
				return "partnerexcel";
			}

			if (partner_id == null) {
				return "redirect:/partner/login";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("partnerId", partner_id);
		return "partnermenu";
	}

	// 엑셀 업로드 페이지
	@GetMapping("/excel")
	public String main(@CookieValue(name = "xcallyadmsession", required = false) String xcallyadmsession,
			HttpSession session, HttpServletResponse response, Model model) {
		String partner_id = null;
		boolean useDtFlag = false;
		try {

			if (xcallyadmsession == null) {
				return "redirect:/partner/login";
			}

			partner_id = (String) session.getAttribute(xcallyadmsession);
			if (partner_id == null) {
				response.setContentType("text/html; charset=euc-kr");
				response.setCharacterEncoding("euc-kr");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('로그인되어있지 않습니다.');location.href='/partner/login';</script>");
			}

			// 사용기한 제크
			useDtFlag = partnerservice.findPartnerUseDt(partner_id);

			if (!useDtFlag) {
				response.setContentType("text/html; charset=euc-kr");
				response.setCharacterEncoding("euc-kr");
				PrintWriter out = response.getWriter();
				out.println("<script>alert('사용 기한이 지났습니다.');location.href='/partner/login';</script>");
				return "partnerexcel";
			}

			if (partner_id == null) {
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
	public SimpleResult readExcel(@RequestParam("file") MultipartFile files, Model model,
			@CookieValue(name = "xcallyadmsession", required = false) String xcallyadmsession, HttpSession session)
			throws IOException {

		String partner_id = null;
		Partner objPartner = null;
		boolean useDtFlag = false;
		SimpleResult simpleResult = new SimpleResult();
		DataResponse<String> dataResponse = new DataResponse<String>();
		
		ArrayList<String> idx_ar =new ArrayList<String>();
		String msg="";
		int suc_cnt=0;
		
		try {
			if (xcallyadmsession == null) {
				simpleResult.setErrCode(999);
				simpleResult.setErrMsg("session null");
			}

			partner_id = (String) session.getAttribute(xcallyadmsession);

			if (partner_id == null) {
				simpleResult.setErrCode(998);
				simpleResult.setErrMsg("session null");
			} else {
				// 사용기한 제크

				useDtFlag = partnerservice.findPartnerUseDt(partner_id);

				if (!useDtFlag) {
					simpleResult.setErrCode(997);
					simpleResult.setErrMsg("사용일자가 지났습니다.");
					return simpleResult;
				}

				// 확장자 검사
				String extension = FilenameUtils.getExtension(files.getOriginalFilename());

				if (!extension.equals("xlsx") && !extension.equals("xls")) {
					throw new IOException("엑셀파일만 업로드 해주세요.");
				}

				Workbook workbook = null;

				if (extension.equals("xlsx")) {
					workbook = new XSSFWorkbook(files.getInputStream());
				} else if (extension.equals("xls")) {
					workbook = new HSSFWorkbook(files.getInputStream());
				}

				// 컬럼인덱스 찾기
				Sheet worksheet = workbook.getSheetAt(0);

				// 컬럼 array
				String[] columnArr = { "인덱스", "묶음주문번호","쇼핑몰", "상품명" , "옵션",
						"수량", "주문자명", "주문자연락처1", "주문자연락처2" ,"수취인명", 
						"수취인연락처1", "수취인연락처2", "수취인우편번호", "수취인주소", "결제"};

				// 컬럼 index array
				int[] indexArr = { -1, -1, -1, -1, -1
								 , -1, -1, -1, -1, -1
								 , -1, -1, -1, -1, -1
								 , -1};

				
				for (int i = 0; i < worksheet.getRow(0).getLastCellNum(); i++) {
					for (int j = 0; j < columnArr.length; j++) {
						if (columnArr[j].equals(worksheet.getRow(0).getCell(i).toString())) {
							indexArr[j] = i;
						}
					}
				}
				
				
				// 묶음주문번호를 저장할 Map
				Map<String, String> shopNoMap = new HashMap<>();

				// 엑셀 파일의 데이터를 읽어서 묶음주문번호를 추출하고 Map에 추가하는 부분
				for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				    Row row = worksheet.getRow(i);

				    String bundleOrderNumber;
				    Cell cell = row.getCell(indexArr[1]); // 해당 셀을 가져옵니다.
				    
				    
				    
				    if (cell != null) {
				    	bundleOrderNumber = cell.getStringCellValue();
				    } else {
				        bundleOrderNumber = ""; // 셀이 null인 경우에 대한 처리를 추가하세요.
				    }
				    
				    
				    // 묶음주문번호가 비어있지 않은 경우에만 Map에 추가합니다.
				    if (!bundleOrderNumber.isEmpty()) {
				        // 이미 Map에 있는 경우에는 넘어갑니다.
				        if (!shopNoMap.containsKey(bundleOrderNumber)) {
				            shopNoMap.put(bundleOrderNumber, String.valueOf(seqService.getSeq()));
				        }
				    }
				}


				for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

					Row row = worksheet.getRow(i);
					//Long order_seq = seqService.getSeq();
					
					// 엑셀에서 묶음주문번호를 가져옵니다.
					String shopNoValue = "";
					String bundleOrderNumber = row.getCell(indexArr[1]).getStringCellValue();

					Long seqNo = seqService.getSeq();
					
					// 묶음주문번호가 Map에 있는 경우에는 해당 값을 가져옵니다.
					if (shopNoMap.containsKey(bundleOrderNumber)) {
					    shopNoValue = shopNoMap.get(bundleOrderNumber);
					} else {
					    // 묶음주문번호가 Map에 없는 경우에는 기본값으로 seqService.getSeq()를 사용합니다.
					    shopNoValue = String.valueOf(seqNo);
					}
					
					
					System.out.println(shopNoValue+" "+i);
					
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			        Date currentDate = new Date();
			        String formattedDate = dateFormat.format(currentDate);
					
			        
			        
					Order order = Order.builder()
							.seqNo(seqNo)
							.partner(partner_id)
							
							.shopNo(shopNoValue)
							.sabangNo(String.valueOf(seqNo))
							
							.mall(row.getCell(indexArr[2]).getStringCellValue())
							.productName(row.getCell(indexArr[3]).getStringCellValue())
							.optionName(row.getCell(indexArr[4]).getStringCellValue())

							.quantity((int) row.getCell(indexArr[5]).getNumericCellValue())
							.ordererName(row.getCell(indexArr[6]).getStringCellValue())
							.ordererPhone1(row.getCell(indexArr[7]).getStringCellValue())
							.ordererPhone2(row.getCell(indexArr[8]).getStringCellValue())

							.receiverName(row.getCell(indexArr[0]).getStringCellValue())
							.receiverPhone1(row.getCell(indexArr[10]).getStringCellValue())
							.receiverPhone2(row.getCell(indexArr[11]).getStringCellValue())
							//.receiverZipcode(row.getCell(indexArr[12]).getStringCellValue())
							.receiverAddress(row.getCell(indexArr[13]).getStringCellValue())
							.payAmt((int) row.getCell(indexArr[14]).getNumericCellValue())
							.orderDt(formattedDate)
							.ordField2("")
							.build();

						
					int rstl = orderservice.InsOrder(order);
					suc_cnt+=rstl;
					if(rstl == 0) {
						idx_ar.add(row.getCell(indexArr[0]).getStringCellValue());
					}
				}

				msg = "총개수:"  + (worksheet.getPhysicalNumberOfRows()-1) 
					+ " 성공개수:" + suc_cnt
					+ " 실패개수:" + (worksheet.getPhysicalNumberOfRows()-suc_cnt-1);
				
				StringBuilder tmp = new StringBuilder();
				
				if(idx_ar.size()!=0) {
					tmp.append(" 실패인덱스:");
					for (int i = 0; i < idx_ar.size(); i++) {
					    tmp.append(idx_ar.get(i));
					    if (i < idx_ar.size() - 1) {
					        tmp.append(",");
					    }
					}
					String result = tmp.toString();
					msg += result;
				}

				
				//System.out.println(rstl);

				simpleResult.setErrCode(0);
				simpleResult.setErrMsg(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			simpleResult.setErrCode(888);
			simpleResult.setErrMsg("Exception");
		}

		return simpleResult;
	}

	@PostMapping("/InsTestOrder")
	@ResponseBody
	public boolean InsTestOrder(String ordName, String ordererPhone1, String receiverAddress,
			String product,String option, String price,String cnt) {
		int rstl = 0;
		try {
			Date now = new Date();

			// Calendar 객체를 사용하여 현재 시간에서 년도를 2099로 설정
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.set(Calendar.YEAR, 2099);

			// SimpleDateFormat을 사용하여 원하는 날짜 형식으로 포맷
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String formattedDate = dateFormat.format(calendar.getTime());

			Order order = Order.builder().seqNo(seqService.getSeq()).partner("xcally")
					.sabangNo(seqService.getSeq().toString()).shopNo(seqService.getSeq().toString()).payAmt(Integer.parseInt(price))
					.quantity(Integer.parseInt(cnt)).productName(product).optionName(option).ordererName(ordName)
					.ordererPhone1(ordererPhone1).ordererPhone2("").receiverName("").receiverPhone1("")
					.receiverPhone2("").receiverAddress(receiverAddress).ordererEmail("").mallOrderDt("").mall("")
					.orderDt(formattedDate).ordField2("").build();
			rstl = orderservice.InsOrder(order);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rstl == 1 ? true : false;
	}

	@GetMapping("/download/excel")
	public ResponseEntity<Resource> downloadExcel() throws IOException {
		// 엑셀 파일을 클래스 패스에서 읽어옵니다. (resources/static/excel/sample.xlsx)
		Resource resource = new ClassPathResource("static/common/sample.xlsx");

		// 파일 이름을 설정합니다.
		String fileName = "sample.xlsx";

		// 응답 헤더를 설정합니다.
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

		// 파일을 읽어와서 ResponseEntity로 감싸서 반환합니다.
		InputStream inputStream = resource.getInputStream();
		return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	@GetMapping("/uploadtest")
	public String uploadtest() {
		return "uploadtest";
	}

	@PostMapping("/uploadrstl")
	public String uploadtest(@RequestParam("file") MultipartFile file, Model model,
			@CookieValue(name = "xcallyadmsession", required = false) String xcallyadmsession, HttpSession session)
			throws IOException {

		 String originalFilename = file.getOriginalFilename();
		    System.out.println("Uploaded File Name: " + originalFilename);

		return "uploadrstl";
	}

	
	//엑셀 에러 확인
	public String makeTable() {
		
		return "";
	}
}
