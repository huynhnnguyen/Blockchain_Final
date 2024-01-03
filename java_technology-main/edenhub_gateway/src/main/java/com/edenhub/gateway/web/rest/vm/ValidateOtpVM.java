package com.edenhub.gateway.web.rest.vm;

import javax.validation.constraints.NotBlank;

/**
 * View Model object for storing the user's key and password.
 */
public class ValidateOtpVM {

	@NotBlank
    private String resetKey;
	
	@NotBlank
	private String otp;

    public String getResetKey() {
		return resetKey;
	}

	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
}
