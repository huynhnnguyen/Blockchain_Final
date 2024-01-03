package com.edenhub.gateway.web.rest.errors;

import com.edenhub.gateway.domain.enumeration.EnumErrors;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
    	super(EnumErrors.EMAIL_ALREADY_USED);
    }
}
