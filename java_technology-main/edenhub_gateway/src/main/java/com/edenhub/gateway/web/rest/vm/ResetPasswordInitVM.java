package com.edenhub.gateway.web.rest.vm;

import javax.validation.constraints.NotBlank;

public class ResetPasswordInitVM {

	@NotBlank
	private String login;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	
}
