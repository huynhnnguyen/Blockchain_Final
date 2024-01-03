package com.edenhub.gateway.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class LoginException extends AbstractThrowableProblem {

	private static final long serialVersionUID = 1L;

	private final String entityName;

	private final String errorKey;

	public LoginException() {
		this(ErrorConstants.DEFAULT_TYPE, "user.login.invalid", "user", "login.invalid");
	}
	
	private LoginException(URI type, String defaultMessage, String entityName, String errorKey) {
		super(type, defaultMessage, Status.UNAUTHORIZED, null, null, null, getAlertParameters(entityName, errorKey, null));
		this.entityName = entityName;
		this.errorKey = errorKey;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getErrorKey() {
		return errorKey;
	}

	private static Map<String, Object> getAlertParameters(String entityName, String errorKey, Object data) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("message", entityName + "." + errorKey);
		parameters.put("params", entityName);
		parameters.put("data", data);
		return parameters;
	}
}
