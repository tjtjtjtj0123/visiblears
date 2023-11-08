package com.xcally.ars.service;

import java.util.List;

import com.xcally.ars.domain.Partner;

public interface PartnerService {
	public Partner findPartner(Partner partner);			//Partner ID, PW 입력 후 로그인
	public boolean findPartnerUseDt(String partnerId);		//사용유효 기간 체크
	public boolean updateSabangNo(Partner partner);			//sabangNo 업데이트
	public Partner getPartnerInfo(String partnerId);		//문의사항 등록을 위한 partner정보 획득
	public List<Partner> getSabangApiUseList();				//사방넷 주문정보 API 사용 업체 리스트
	public Partner getSabangApiUsePartner(String partnerId);//api 동작햇는지 여부 체크
}
