package com.edenhub.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Edenhub Gateway.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    public String secretKey;

	public OTPObject otp;

	public OTPObject getOtp() {
		return otp;
	}

	public void setOtp(OTPObject otp) {
		this.otp = otp;
	}

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public static class OTPObject {
		private Integer expirationTime;
		private Integer limitResend;
		private Integer limitIncorrect;
		public Integer getExpirationTime() {
			return expirationTime;
		}
		public void setExpirationTime(Integer expirationTime) {
			this.expirationTime = expirationTime;
		}
		public Integer getLimitResend() {
			return limitResend;
		}
		public void setLimitResend(Integer limitResend) {
			this.limitResend = limitResend;
		}
		public Integer getLimitIncorrect() {
			return limitIncorrect;
		}
		public void setLimitIncorrect(Integer limitIncorrect) {
			this.limitIncorrect = limitIncorrect;
		}
		
	}
	
}
