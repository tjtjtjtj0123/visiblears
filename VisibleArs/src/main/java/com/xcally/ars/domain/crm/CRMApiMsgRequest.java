package com.xcally.ars.domain.crm;

import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CRMApiMsgRequest {
	private String partner;
    private String comid;
    private String keycode;
    private String hp;
    private String title;
    
    private String inquiryType;
    private String msg;
    private String proctime;
    private String seq;
    private ArrayList<String> fileNameList;
}