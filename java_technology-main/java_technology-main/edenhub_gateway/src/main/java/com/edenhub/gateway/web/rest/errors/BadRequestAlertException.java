package com.edenhub.gateway.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import com.edenhub.gateway.domain.enumeration.EnumErrors;

public class BadRequestAlertException extends AbstractThrowableProblem {

	private static final long serialVersionUID = 1L;

	private final String entityName;

	private final String errorKey;

	public BadRequestAlertException(EnumErrors error) {
		this(ErrorConstants.DEFAULT_TYPE, error.getMessage(), error.getEntityName(), error.getErrorKey());
	}

	public BadRequestAlertException(EnumErrors error, Object data) {
		this(ErrorConstants.DEFAULT_TYPE, error.getMessage(), error.getEntityName(), error.getErrorKey(), data);
	}
	
	public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

	private BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey) {
		super(type, defaultMessage, Status.BAD_REQUEST, null, null, null,
				getAlertParameters(entityName, errorKey, null));
		this.entityName = entityName;
		this.errorKey = errorKey;
	}

	private BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey, Object data) {
		super(type, defaultMessage, Status.BAD_REQUEST, null, null, null,
				getAlertParameters(entityName, errorKey, data));
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
