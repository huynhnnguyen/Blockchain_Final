package com.edenhub.gateway.web.rest;

import com.edenhub.gateway.config.ApplicationProperties;
import com.edenhub.gateway.domain.enumeration.EnumErrors;
import com.edenhub.gateway.domain.enumeration.SMSTemplate;
import com.edenhub.gateway.service.OtpService;
import com.edenhub.gateway.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class OtpResource {

    private final Logger log = LoggerFactory.getLogger(OtpResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private OtpService otpExtService;

    @Autowired
    private ApplicationProperties properties;

    @PostMapping("/otp/{type}/init/{loginName}")
    public ResponseEntity<Map<String, Integer>> initOTP(
            @PathVariable("type") String type,
            @PathVariable("loginName") String loginName) {
        log.debug("REST request to init OTP from service");
        String lang = "vi";
        String appName = null;
        switch (type) {
            case "register":
                otpExtService.sendOtp(lang, loginName, SMSTemplate.REGISTER, appName);
                break;
            case "change-account":
                otpExtService.sendOtp(lang, loginName, SMSTemplate.CHANGEACCOUNT, appName);
                break;
            case "forgot":
                otpExtService.sendOtp(lang, loginName, SMSTemplate.RESTPASSWORD, appName);
                break;
            default:
                throw new BadRequestAlertException(EnumErrors.EXCEPTION, "This API is not supported");
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("expirationTime", properties.getOtp().getExpirationTime() - 5);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/otp/validate/{loginName}/{otp}")
    public ResponseEntity<Map<String, String>> validateOTP(@PathVariable("otp") String otp,
                                                           @PathVariable("loginName") String loginName) {
        log.debug("REST request to validate OTP from service");
        Map<String, String> result = otpExtService.validateOtp(otp, loginName);
        return ResponseEntity.ok().body(result);
    }
}
