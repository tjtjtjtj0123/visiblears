package com.xcally.ars.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PartnerLogin {
	@JsonProperty("partner_id")
    private String partnerId; // 파트너 아이디
	
	@JsonProperty("partner_pw")
    private String partnerPw; // 파트너 패스워드
}
