package com.edenhub.gateway.domain.enumeration;

public enum EnumErrors {

    USER_ACCESS_DENIED("User", "AccessDenied", ""),

	PASSWORD_INCORRECT("password", "incorrect", ""), 
	ACCOUNT_NOT_FOUND("account", "notfound", ""), 
	RESETKEY_INVALID("resetkey", "invalid", ""), 
	ACTIVATION_KEY_INCORRECT("activationkey", "incorrect", null), 
	OTP_INCORRECT("otp", "incorrect", null), 
	EMAIL_REQUIRED("email", "required", null), 
	CREATE_ACCOUNT_FAILED("account", "createfailed", null), 
	LIMIT_OTP("otp", "limited", null), 
	USER_NOT_FOUND("user", "notfound", null), 
//	LOGIN_INVALID("login", "login.invalid", null), 
	ID_EXISTS("id", "exists", null), 
	LOGIN_NAME_ALREADY_USED("loginname", "already.used", null), 
	EMAIL_ALREADY_USED("email", "already.used", null), 
	LOGIN_NAME_INVALID("loginname", "invalid", "Login name must be phone or email"), 
	OTP_EXPIRED("opt", "expired", "Otp has expired"), 
	USER_NOT_ACTIVATED("user", "notactivated", "User not activated"),
	EXCEPTION("exception", "exception", ""),
	OTP_LIMITED_RESEND("OTP", "SendGotLimited", ""),
	OTP_LIMITED_VALIDATE("OTP", "ValidateGotLimited", ""),
	OTP_NOT_FOUND("OTP", "NotFound", "");
		
	// ... add more cases here ...
	private final String entityName;
	private final String errorKey;
	private String message;

	public String getMessage() {
		message = getEntityName() + "." + getErrorKey();
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getErrorKey() {
		return errorKey;
	}

	EnumErrors(String entityName, String errorKey, String description) {
		this.entityName = entityName;
		this.errorKey = errorKey;
	}

}
