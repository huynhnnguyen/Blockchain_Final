package com.edenhub.gateway.service.proxy.payload;



import com.edenhub.gateway.domain.enumeration.SMSTemplate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SmsOtpRequestDTO extends BaseSms {
    @NotBlank
    private String otp;
    @NotNull
    private SMSTemplate template;

    public SmsOtpRequestDTO() {
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public SMSTemplate getTemplate() {
        return template;
    }

    public void setTemplate(SMSTemplate template) {
        this.template = template;
    }
}
