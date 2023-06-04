package com.xcally.ars.repository;

import org.apache.ibatis.annotations.Mapper;

import com.xcally.ars.domain.Partner;

@Mapper
public interface PartnerMapper {
	public Partner findPartner(Partner partner);
	public String findPartnerUseDt(String partnerId);
	public int updateSabangNo(Partner parnter);
}
