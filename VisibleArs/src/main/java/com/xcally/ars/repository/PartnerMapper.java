package com.xcally.ars.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xcally.ars.domain.Order;
import com.xcally.ars.domain.Partner;

@Mapper
public interface PartnerMapper {
	public Partner findPartner(Partner partner);
	public String findPartnerUseDt(String partnerId);
	public int updateSabangNo(Partner parnter);
	public Partner getPartnerInfo(String partnerId);
	public List<Partner> getSabangApiUseList();
	public Partner getSabangApiUsePartner(String partnerId);
}
