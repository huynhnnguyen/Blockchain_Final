package com.edenhub.gateway.service.proxy.payload;

import javax.validation.constraints.NotBlank;

public class BaseSms {
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String secretKey;

    public BaseSms() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
