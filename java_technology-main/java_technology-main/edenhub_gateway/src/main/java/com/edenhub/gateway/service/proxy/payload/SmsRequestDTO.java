package com.edenhub.gateway.service.proxy.payload;

import javax.validation.constraints.NotBlank;

public class SmsRequestDTO extends BaseSms {
    @NotBlank
    private String message;

    public SmsRequestDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
