package com.xcally.ars.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiLog {
    private Long apiLogSeq;
    private Long boardSeq; // 문의글번호
    private String partner;
    private String endPoint;
    private String request;
    private String response;
    private Boolean success;
    private String errorMessage;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    
}
