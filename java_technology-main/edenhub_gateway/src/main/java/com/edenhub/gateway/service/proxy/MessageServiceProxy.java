package com.edenhub.gateway.service.proxy;

import com.edenhub.gateway.service.proxy.payload.SmsOtpRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "godi-message", url = "http://localhost:8184/")
public interface MessageServiceProxy {

//	@PostMapping(value = "/api/v1/mail/send", produces = "application/json")
//	void sendMail(MailRequestPayload request);
	
	@PostMapping(value = "/api/v1/sms/send/otp", produces = "application/json")
	void senSMS(SmsOtpRequestDTO request);

}
