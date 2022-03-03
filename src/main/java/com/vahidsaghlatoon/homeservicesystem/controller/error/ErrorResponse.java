package com.vahidsaghlatoon.homeservicesystem.controller.error;

import lombok.*;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private String message ;
    private Instant time ;
}
