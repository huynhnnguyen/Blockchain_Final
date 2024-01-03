package com.edenhub.gateway.web.rest;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.edenhub.gateway.domain.User;
import com.edenhub.gateway.domain.enumeration.EnumErrors;
import com.edenhub.gateway.security.AuthoritiesConstants;
import com.edenhub.gateway.security.jwt.JWTFilter;
import com.edenhub.gateway.service.UserService;
import com.edenhub.gateway.service.dto.PasswordChangeDTO;
import com.edenhub.gateway.service.dto.UserDTO;
import com.edenhub.gateway.service.proxy.SysServiceProxy;
import com.edenhub.gateway.web.rest.UserJWTController.JWTToken;
import com.edenhub.gateway.web.rest.errors.BadRequestAlertException;
import com.edenhub.gateway.web.rest.errors.EmailAlreadyUsedException;
import com.edenhub.gateway.web.rest.errors.InvalidPasswordException;
import com.edenhub.gateway.web.rest.errors.LoginAlreadyUsedException;
import com.edenhub.gateway.web.rest.errors.NotFoundException;
import com.edenhub.gateway.web.rest.vm.KeyAndPasswordVM;
import com.edenhub.gateway.web.rest.vm.LoginVM;
import com.edenhub.gateway.web.rest.vm.ManagedUserVM;
import com.edenhub.gateway.web.rest.vm.ResendOtpForResetVM;
import com.edenhub.gateway.web.rest.vm.ResetPasswordInitVM;
import com.edenhub.gateway.web.rest.vm.ValidateOtpVM;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/v1/sys/account")
public class AccountSysResource {

	private final Logger log = LoggerFactory.getLogger(AccountSysResource.class);

	private final SysServiceProxy sysServiceProxy;
	
	private final UserService userService;

	public AccountSysResource(UserService userService, SysServiceProxy sysServiceProxy) {
		super();
		this.userService = userService;
		this.sysServiceProxy = sysServiceProxy;
	}

	@PostMapping("/authenticate")
	public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
		String jwt = userService.login(loginVM);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
		try {
			sysServiceProxy.logLoginTime("Bearer " + jwt);
		} catch (Exception e) {
			log.error("Log login time failed: {}", e.getMessage());
		}
		return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
	}

	/**
	 * {@code POST  /register} : register the user.
	 *
	 * @param managedUserVM the managed user View Model.
	 * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password
	 *                                   is incorrect.
	 * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
	 *                                   already used.
	 * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is
	 *                                   already used.
	 */
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<User> registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
		if (!checkPasswordLength(managedUserVM.getPassword())) {
			throw new BadRequestAlertException(EnumErrors.PASSWORD_INCORRECT);
		}
		Set<String> authors = new HashSet<>();
		authors.add(AuthoritiesConstants.SYS);
		managedUserVM.setAuthorities(authors);
		User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
		return ResponseEntity.ok().body(user);
	}

	@GetMapping("/register/check/{phonenumber}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<User> checkPhone(@PathVariable(name = "phonenumber") String phonenumber) {
		userService.checkPhone(phonenumber);
		return ResponseEntity.noContent().build();
	}

	/**
	 * {@code GET  /activate} : activate the registered user.
	 *
	 * @param activationKey the activation key.
	 * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
	 *                          couldn't be activated.
	 */
	@GetMapping("/activate")
	public ResponseEntity<Void> activateAccount(@RequestParam(value = "activationKey") String activationKey, @RequestParam(value = "otp") String otp) {
		userService.activateRegistration(activationKey, otp);
		return ResponseEntity.noContent().build();
	}

	/**
	 * {@code POST   /account/reset-password/init} : Send an email to reset the
	 * password of the user.
	 *
	 * @param mail the mail of the user.
	 */
	@PostMapping(path = "/reset-password/init")
	public User requestPasswordReset(@Valid @RequestBody ResetPasswordInitVM resetDTO) {
		return userService.requestPasswordReset(resetDTO.getLogin());
	}

	/**
	 * Resend otp to the phone number
	 * 
	 * @param resendOtpDTO
	 * @return
	 */
	@PostMapping(path = "/reset-password/resend-otp")
	public ResponseEntity<User> resendOtpReset(@Valid @RequestBody ResendOtpForResetVM resendOtpDTO) {
		log.debug("REST request to resend otp: {}", resendOtpDTO);
		User user = userService.resendOtpForReset(resendOtpDTO.getResetKey());
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	/**
	 * {@code POST   /account/reset-password/finish} : Finish to reset the password
	 * of the user.
	 *
	 * @param keyAndPassword the generated key and the new password.
	 * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is
	 *                                  incorrect.
	 * @throws RuntimeException         {@code 500 (Internal Server Error)} if the
	 *                                  password could not be reset.
	 */
	@PostMapping(path = "/reset-password/finish")
	public ResponseEntity<User> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
		if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
			throw new InvalidPasswordException();
		}
		userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getResetKey());
		return ResponseEntity.noContent().build();
	}

	/**
	 * Check opt
	 * 
	 * @return
	 */
	@PostMapping(path = "/reset-password/validate-otp")
	public ResponseEntity<Object> validateOtp(@Valid @RequestBody ValidateOtpVM dto) {
		log.debug("REST request to validate otp: {}", dto);
		Object validateOtp = userService.validateOtp(dto);
		return ResponseEntity.ok().body(validateOtp);
	}

	private static boolean checkPasswordLength(String password) {
		return !StringUtils.isEmpty(password) && password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH
				&& password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
	}

	/**
	 * {@code GET  /account} : get the current user.
	 *
	 * @return the current user.
	 * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
	 *                          couldn't be returned.
	 */
	@GetMapping()
	public UserDTO getAccount() {
		try {
			sysServiceProxy.logLoginTime();
		} catch (Exception e) {
			log.error("Log login time failed: {}", e.getMessage());
		}
		return userService.getUserWithAuthorities().map(UserDTO::new)
				.orElseThrow(() -> new NotFoundException(EnumErrors.ACCOUNT_NOT_FOUND));
	}
	
	/**
	 * {@code POST  /account/change-password} : changes the current user's password.
	 *
	 * @param passwordChangeDto current and new password.
	 * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new
	 *                                  password is incorrect.
	 */
	@PostMapping(path = "/change-password")
	public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
		if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
			throw new InvalidPasswordException();
		}
		userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
	}
}
