package com.edenhub.gateway.web.rest.vm;

import javax.validation.constraints.NotBlank;

public class ResendOtpForResetVM {
	@NotBlank
	private String resetKey;
	
	public String getResetKey() {
		return resetKey;
	}
	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}
}
