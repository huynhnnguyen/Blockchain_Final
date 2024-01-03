package com.edenhub.gateway.service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.edenhub.gateway.domain.enumeration.SMSTemplate;
import com.edenhub.gateway.service.proxy.MessageServiceProxy;
import com.edenhub.gateway.service.proxy.payload.SmsOtpRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.edenhub.gateway.config.ApplicationProperties;
import com.edenhub.gateway.domain.Otp;
import com.edenhub.gateway.domain.enumeration.EnumErrors;
import com.edenhub.gateway.repository.OtpRepository;
import com.edenhub.gateway.utils.ValidatorUtils;
import com.edenhub.gateway.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.security.RandomUtil;

/**
 * Service Implementation for managing {@link Otp}.
 */
@Service
@Transactional
public class OtpService {

	private final Logger log = LoggerFactory.getLogger(OtpService.class);

	@Autowired
	private OtpRepository otpExtRepository;

	@Autowired
	private ApplicationProperties properties;

    @Autowired
    private MessageServiceProxy messageServiceProxy;

	@Autowired
	private Environment env;

	private boolean fakeOTP() {
		return !Arrays.asList(env.getActiveProfiles()).contains("prod");
	}

	public Optional<Otp> saveOtp(Otp otp) {
		return Optional.of(otpExtRepository.save(otp));
	}

	public void sendOtp(String lang, String username, SMSTemplate template, String appName) {
		Optional<Otp> optOTP = findByUserId(username);
		Otp otpExtDTO = null;
		if (!optOTP.isPresent()) {
			otpExtDTO = new Otp();
			otpExtDTO.setUserName(username);
			otpExtDTO.setResendOtp(0);
			otpExtDTO.setIncorrectOtp(0);
		} else {
			Otp otp = optOTP.get();
			Instant expiredTime = otp.getLastModifiedDate().plus(Duration.ofDays(1));
			if (expiredTime.compareTo(Instant.now()) < 0) {
				otp.setResendOtp(0);
			}
			if (otp.getResendOtp() >= properties.getOtp().getLimitResend()) {
				throw new BadRequestAlertException(EnumErrors.OTP_LIMITED_RESEND);
			}
			otpExtDTO = otp;
		}
		otpExtDTO.setResendOtp(otpExtDTO.getResendOtp() + 1);
		otpExtDTO.setOtp(ValidatorUtils.generateOtp(fakeOTP()));
		log.debug("Init Otp: {}, for user: {}", otpExtDTO.getOtp(), username);
		if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
			if (ValidatorUtils.validateEmail(username)) {
//                messageServiceProxy.sendEmail();
			} else if (ValidatorUtils.validatePhone(username)) {
                SmsOtpRequestDTO request = new SmsOtpRequestDTO();
                request.setOtp(otpExtDTO.getOtp());
                request.setPhoneNumber(username);
                request.setSecretKey(properties.secretKey);
                request.setTemplate(template);
                messageServiceProxy.senSMS(request);
			} else {
				throw new BadRequestAlertException(EnumErrors.ACCOUNT_NOT_FOUND);
			}
		}
		otpExtRepository.save(otpExtDTO);

	}

	@Transactional(readOnly = true)
	public Optional<Otp> findByUserId(String userName) {
		log.debug("Request to get OTP : userId={}", userName);
		return otpExtRepository.findByUserName(userName);
	}

	@Transactional(readOnly = true)
	public Optional<Otp> findByUserIdAndActivationKey(String userName, String activeKey) {
		log.debug("Request to get OTP : userId={}", userName);
		return otpExtRepository.findByUserNameAndActiveKey(userName, activeKey);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = BadRequestAlertException.class)
	public Map<String, String> validateOtp(String otpCode, String userName) {
		log.debug("Request to validate otp");
		Map<String, String> activeKey = new HashMap<>();
		Optional<Otp> optOTP = otpExtRepository.findByUserName(userName);
		if (!optOTP.isPresent()) {
			throw new BadRequestAlertException(EnumErrors.ACCOUNT_NOT_FOUND);
		}
		Otp otp = optOTP.get();
		Instant expiredTime = otp.getLastModifiedDate().plus(Duration.ofDays(1));
		if (otp.getIncorrectOtp() >= properties.getOtp().getLimitIncorrect()){
            Instant now = Instant.now();
            if(Math.abs(ChronoUnit.HOURS.between(expiredTime, now)) >= 24){
                otp.setIncorrectOtp(0);
            } else if(expiredTime.compareTo(Instant.now()) > 0) {
                throw new BadRequestAlertException(EnumErrors.OTP_LIMITED_VALIDATE);
            }
		}
		if (otpCode.equals(otp.getOtp())) {
			Instant expired = otp.getLastModifiedDate().plusSeconds(properties.getOtp().getExpirationTime());
			if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
				if (Instant.now().isAfter(expired))
					throw new BadRequestAlertException(EnumErrors.OTP_EXPIRED);
			}
			otp.setIncorrectOtp(0);
			otp.setActiveKey(RandomUtil.generateActivationKey());
			activeKey.put("resetKey", otp.getActiveKey());
			otpExtRepository.save(otp);
		} else {
			otp.setIncorrectOtp(otp.getIncorrectOtp() == null ? 1 : otp.getIncorrectOtp() + 1);
			otpExtRepository.saveAndFlush(otp);
			throw new BadRequestAlertException(EnumErrors.OTP_INCORRECT);
		}
		return activeKey;
	}

	@Transactional(readOnly = true)
	public Otp findDtoByUserId(String userName) {
		return otpExtRepository.findByUserName(userName)
				.orElseThrow(() -> new BadRequestAlertException(EnumErrors.OTP_NOT_FOUND));
	}

}
