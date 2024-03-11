package com.luxshan.authentication.auth.dto;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
