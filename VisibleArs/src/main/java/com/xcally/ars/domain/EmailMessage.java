package com.xcally.ars.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class EmailMessage {

    private String to;
    private String subject;
    private String message;
}