package com.edenhub.gateway.web.rest;

import com.edenhub.gateway.domain.User;
import com.edenhub.gateway.domain.enumeration.EnumErrors;
import com.edenhub.gateway.security.AuthoritiesConstants;
import com.edenhub.gateway.security.jwt.JWTFilter;
import com.edenhub.gateway.service.UserService;
import com.edenhub.gateway.service.dto.PasswordChangeDTO;
import com.edenhub.gateway.service.dto.UserDTO;
import com.edenhub.gateway.service.proxy.SysServiceProxy;
import com.edenhub.gateway.web.rest.UserJWTController.JWTToken;
import com.edenhub.gateway.web.rest.errors.*;
import com.edenhub.gateway.web.rest.vm.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/v1/admin/account")
public class AccountAdminResource {

	private final Logger log = LoggerFactory.getLogger(AccountAdminResource.class);

	private final SysServiceProxy sysServiceProxy;

	private final UserService userService;

	public AccountAdminResource(UserService userService, SysServiceProxy sysServiceProxy) {
		super();
		this.userService = userService;
		this.sysServiceProxy = sysServiceProxy;
	}

	@PostMapping("/authenticate")
	public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
		String jwt = userService.loginByAdmin(loginVM);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
		try {
			sysServiceProxy.logLoginTime("Bearer " + jwt);
		} catch (Exception e) {
			log.error("Log login time failed: {}", e.getMessage());
		}
		return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
	}
}
