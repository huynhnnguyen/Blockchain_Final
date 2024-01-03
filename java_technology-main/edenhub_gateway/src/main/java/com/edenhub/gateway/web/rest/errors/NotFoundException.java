package com.edenhub.gateway.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import com.edenhub.gateway.domain.enumeration.EnumErrors;

public class NotFoundException extends AbstractThrowableProblem {

	private static final long serialVersionUID = 1L;

	private final String entityName;

	private final String errorKey;

	private final Object data;

	public NotFoundException(EnumErrors error) {
		this(ErrorConstants.DEFAULT_TYPE, error.getMessage(), error.getEntityName(), error.getErrorKey());
	}

	public NotFoundException(EnumErrors error, Object data) {
		this(ErrorConstants.DEFAULT_TYPE, error.getMessage(), error.getEntityName(), error.getErrorKey(), data);

	}

	public NotFoundException(String defaultMessage, String entityName, String errorKey) {
		this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
	}
	
	//Binh yeu cau not found tra ve code 400
	public NotFoundException(URI type, String defaultMessage, String entityName, String errorKey) {
		super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey, null));
		this.entityName = entityName;
		this.errorKey = errorKey;
		this.data = new Object();
	}

	//Binh yeu cau not found tra ve code 400
	public NotFoundException(URI type, String defaultMessage, String entityName, String errorKey, Object data) {
		super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey, data));
		this.entityName = entityName;
		this.errorKey = errorKey;
		this.data = data;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getErrorKey() {
		return errorKey;
	}

	public Object getData() {
		return data;
	}

	private static Map<String, Object> getAlertParameters(String entityName, String errorKey, Object data) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("message", entityName + "." + errorKey);
		parameters.put("params", entityName);
		parameters.put("data", data);
		return parameters;
	}
}
