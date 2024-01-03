package com.edenhub.gateway.web.rest.errors;

import com.edenhub.gateway.domain.enumeration.EnumErrors;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
    	super(EnumErrors.LOGIN_NAME_ALREADY_USED);
    }
}
