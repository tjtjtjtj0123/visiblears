package com.xcally.ars.domain.crm;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CRMApiMsgResponse {
    private String state;
    private String code;
    private String message;

}