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
public class ExceptionLog {
    private Long exceptionLogSeq;
    private String partner;
    private String exceptionMessage;
    private String exceptionStackTrace;
    private LocalDateTime regDt; 	   // 배치 시간

}