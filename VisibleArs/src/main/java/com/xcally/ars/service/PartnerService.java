package com.xcally.ars.service;

import com.xcally.ars.domain.Partner;

public interface PartnerService {
	public Partner findPartner(Partner partner);
	public boolean findPartnerUseDt(String partnerId);
	public boolean updateSabangNo(Partner partner);
	public Partner getPartnerInfo(String partnerId);
}
