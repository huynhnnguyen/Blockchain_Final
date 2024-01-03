package com.edenhub.gateway.service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.edenhub.gateway.config.ApplicationProperties;
import com.edenhub.gateway.domain.enumeration.SMSTemplate;
import com.edenhub.gateway.service.dto.ProfileAccountPayload;
import com.edenhub.gateway.service.proxy.MessageServiceProxy;
import com.edenhub.gateway.service.proxy.payload.SmsOtpRequestDTO;
import com.edenhub.gateway.service.serviceitf.UserServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.edenhub.gateway.config.Constants;
import com.edenhub.gateway.domain.Authority;
import com.edenhub.gateway.domain.User;
import com.edenhub.gateway.domain.enumeration.EnumErrors;
import com.edenhub.gateway.repository.AuthorityRepository;
import com.edenhub.gateway.repository.UserRepository;
import com.edenhub.gateway.security.AuthoritiesConstants;
import com.edenhub.gateway.security.SecurityUtils;
import com.edenhub.gateway.security.jwt.TokenProvider;
import com.edenhub.gateway.service.dto.AccountLinkDTO;
import com.edenhub.gateway.service.dto.UserDTO;
import com.edenhub.gateway.service.proxy.SysServiceProxy;
import com.edenhub.gateway.utils.ValidatorUtils;
import com.edenhub.gateway.web.rest.errors.BadRequestAlertException;
import com.edenhub.gateway.web.rest.errors.LoginException;
import com.edenhub.gateway.web.rest.errors.NotFoundException;
import com.edenhub.gateway.web.rest.vm.LoginVM;
import com.edenhub.gateway.web.rest.vm.ValidateOtpVM;

import io.github.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService implements UserServiceInterface {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final SysServiceProxy sysServiceProxy;

    private final MessageServiceProxy messageServiceProxy;

    private final ApplicationProperties properties;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

	private final TokenProvider tokenProvider;

    private final CacheManager cacheManager;

    private final OtpService otpService;

    public UserService(MessageServiceProxy messageServiceProxy, ApplicationProperties properties, UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthorityRepository authorityRepository, AuthenticationManagerBuilder authenticationManagerBuilder,
                       TokenProvider tokenProvider, CacheManager cacheManager, SysServiceProxy sysServiceProxy, OtpService otpService) {
		super();
        this.messageServiceProxy = messageServiceProxy;
        this.properties = properties;
        this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorityRepository = authorityRepository;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.tokenProvider = tokenProvider;
		this.cacheManager = cacheManager;
		this.sysServiceProxy = sysServiceProxy;
		this.otpService = otpService;
	}

	public String login(LoginVM loginVM) {
    	 UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        Authentication authentication = null;
		try {
			authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		}catch (InternalAuthenticationServiceException e) {
			throw new BadRequestAlertException(EnumErrors.USER_NOT_ACTIVATED);
		} catch (BadCredentialsException e) {
			throw new LoginException();
		}
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findOneByLogin(loginVM.getUsername()).orElseThrow(
                ()-> new BadRequestAlertException(EnumErrors.LOGIN_NAME_INVALID)
        );
        List<String> authorityByUserId = userRepository.findUserAuthorityByUserId(user.getId());
        if(!authorityByUserId.contains(AuthoritiesConstants.SYS )|| authorityByUserId.contains(AuthoritiesConstants.ADMIN)) {
            throw new BadRequestAlertException(EnumErrors.USER_ACCESS_DENIED);
        }
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        return tokenProvider.createToken(authentication, rememberMe);
    }


    public String loginByAdmin(LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        Authentication authentication = null;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        }catch (InternalAuthenticationServiceException e) {
            throw new BadRequestAlertException(EnumErrors.USER_NOT_ACTIVATED);
        } catch (BadCredentialsException e) {
            throw new LoginException();
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findOneByLogin(loginVM.getUsername()).orElseThrow(
                ()-> new BadRequestAlertException(EnumErrors.LOGIN_NAME_INVALID)
        );
        List<String> authorityByUserId = userRepository.findUserAuthorityByUserId(user.getId());
        if(!authorityByUserId.contains(AuthoritiesConstants.ADMIN )) {
            throw new BadRequestAlertException(EnumErrors.USER_ACCESS_DENIED);
        }
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        return tokenProvider.createToken(authentication, rememberMe);
    }

    public void activateRegistration(String activationKey) {
        log.debug("Activating user for activation key {}", activationKey);
        userRepository.findOneByActivationKey(activationKey)
            .map(u -> {
                // activate given user for the registration key.
                u.setActivated(true);
                u.setActivationKey(null);
                this.clearUserCaches(u);
                log.debug("Activated user: {}", u);
                return u;
            }).orElseThrow(() -> new BadRequestAlertException(EnumErrors.ACTIVATION_KEY_INCORRECT));
    }

    public void activateRegistration(String activationKey, String otp) {
    	log.debug("Activating user for activation key {}", activationKey);
    	User user = userRepository.findOneByActivationKey(activationKey)
			.map(u -> {
				if(u.getLastResendOtp().isBefore(Instant.now().minusSeconds(295))) {
					throw new BadRequestAlertException(EnumErrors.OTP_EXPIRED);
				}
				if (!otp.equals(u.getOtp())) {
					throw new BadRequestAlertException(EnumErrors.OTP_INCORRECT);
				}
				u.setActivated(true);
				u.setActivationKey(null);
				u.setOtp(null);
				this.clearUserCaches(u);
				log.debug("Activated user: {}", u);
				return u;
			}).orElseThrow(() -> new BadRequestAlertException(EnumErrors.ACTIVATION_KEY_INCORRECT));
    	if(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()).contains(AuthoritiesConstants.SYS)) {
    		log.debug("Create accout link for the sys user: user={}", user);
    		AccountLinkDTO accoutLink = new AccountLinkDTO(user.getId(), user.getLogin(), user.getFullName(), null);
    		sysServiceProxy.createSysAccoutLink(accoutLink);
    	}
    }

    public void activateRegistration(User userActivated) {
    	User user = Optional.of(userActivated)
    			.map(u -> {
    				if(u.getLastResendOtp().isBefore(Instant.now().minusSeconds(86400))) {
    					throw new BadRequestAlertException(EnumErrors.OTP_EXPIRED);
    				}
    				// activate given user for the registration key.
    				/*
    				 * TODO remove comment line after finish the feature sent otp
				if (!dto.getOtp().equals(user.getOtp())) {
					throw new BadRequestAlertException(EnumErrors.OTP_INCORRECT);
				}
    				 */
    				u.setActivated(true);
    				u.setActivationKey(null);
    				u.setOtp(null);
    				this.clearUserCaches(u);
    				log.debug("Activated user: {}", u);
    				return u;
    			}).orElseThrow(() -> new BadRequestAlertException(EnumErrors.ACTIVATION_KEY_INCORRECT));
    	if(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()).contains(AuthoritiesConstants.SYS)) {
    		log.debug("Create accout link for the sys user: user={}", user);
    		AccountLinkDTO accoutLink = new AccountLinkDTO(user.getId(), user.getLogin(), user.getFullName(), null);
    		sysServiceProxy.createSysAccoutLink(accoutLink);
    	}
    }

    public void completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            }).orElseThrow(() -> new BadRequestAlertException(EnumErrors.ACTIVATION_KEY_INCORRECT));
    }

    public User requestPasswordReset(String login) {
    	log.debug("Request to reset password: login={}", login);
        return userRepository.findOneByLogin(login)
            .filter(User::getActivated)
            .map(u -> {
                u.setResetKey(RandomUtil.generateResetKey());
                u.setResetDate(Instant.now());
                updateOtp(u, SMSTemplate.RESTPASSWORD);
                this.clearUserCaches(u);
                return u;
            }).orElseThrow(() -> new NotFoundException(EnumErrors.USER_NOT_FOUND));
    }

    public User registerUser(UserDTO userDTO, String password) {
    	validateLoginRegister(userDTO);
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setActivationKey(RandomUtil.generateActivationKey());
		if (userDTO.getLangKey() == null) {
			newUser.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
		} else {
			newUser.setLangKey(userDTO.getLangKey());
		}
		newUser.setOtp(generateOtp());
		newUser.setNumberSendOtp(1);
		newUser.setLastResendOtp(Instant.now());
		if(userDTO.getActivationKey() != null) {
			//This user has been verified OTP before create account.
			newUser.setActivated(true);
		} else {
			// new user is not active
			newUser.setActivated(false);
		}
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
		userDTO.getAuthorities().forEach(autority -> {
			authorityRepository.findById(autority).ifPresent(authorities::add);
		});
		newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        //Send active email or otp
//        if(ValidatorUtils.validateEmail(userDTO.getLogin())) {
//			log.debug("Send email to the user: {}", userDTO.getLogin());
//		}else if(ValidatorUtils.validatePhone(userDTO.getLogin())) {
//			log.debug("Send otp to the phone number: {}", userDTO.getLogin());
//            sendOtp(newUser, SMSTemplate.REGISTER);
//		}else {
//			throw new BadRequestAlertException(EnumErrors.LOGIN_NAME_INVALID);
//		}
        if (userDTO.getAuthorities().contains(AuthoritiesConstants.SYS)) {
            log.debug("Create account link for the sys user: user={}", newUser);
            AccountLinkDTO accoutLink = new AccountLinkDTO(newUser.getId(), newUser.getLogin(), newUser.getFullName(), null);
            sysServiceProxy.createSysAccoutLink(accoutLink);
        }
        return newUser;
    }

	/**
	 * Validate the email and set login name as email into the userDTO
	 *
	 * @param userDTO
	 */
	private void validateLoginRegister(UserDTO userDTO) {
		if(userDTO.getActivationKey() != null) {
			otpService.findByUserIdAndActivationKey(userDTO.getLogin(), userDTO.getActivationKey())
			.orElseThrow(() -> new BadRequestAlertException(EnumErrors.EXCEPTION, "Invalid Activation Key"));
		}
		String login;
		if(ValidatorUtils.validateEmail(userDTO.getLogin())) {
			login = userDTO.getLogin().toLowerCase();
			userDTO.setEmail(login);
		}else if(ValidatorUtils.validatePhone(userDTO.getLogin())) {
			login = userDTO.getLogin();
		}else {
			throw new BadRequestAlertException(EnumErrors.LOGIN_NAME_INVALID);
		}
		userRepository.findOneByLogin(login).ifPresent(existingUser -> {
			boolean removed = removeNonActivatedUser(existingUser);
			if (!removed) {
				throw new BadRequestAlertException(EnumErrors.EMAIL_ALREADY_USED);
			}
		});
		userDTO.setLogin(login);
	}

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
             return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail()!= null) {
            user.setEmail((userDTO.getEmail()).toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.getPassword()== null) {
            String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
            user.setPassword(encryptedPassword);
        } else {
            String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encryptedPassword);
        }
        user.setResetKey(RandomUtil.generateResetKey());
        user.setLastResendOtp(Instant.now());
        user.setNumberSendOtp(0);
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        createUser(user);
        return user;
    }

    public void createUser(User user) {
        ProfileAccountPayload profileAccountPayload = new ProfileAccountPayload();
        profileAccountPayload.setName(user.getFirstName());
        profileAccountPayload.setShortName(user.getLastName());
        profileAccountPayload.setPhone(user.getLogin());
        profileAccountPayload.setEmail(user.getEmail());
        profileAccountPayload.setLogo(user.getImageUrl());
        //TODO call API to create employees account
        sysServiceProxy.createSysEmployeesAccount(profileAccountPayload);
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    @Override
    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            List<User> userList = userRepository.findAllByCreatedBy(login);
            for(User user1: userList) {
                sysServiceProxy.deleteAccountLink(user1.getLogin());
            }
            userRepository.delete(user);
            sysServiceProxy.deleteAccountLink(login);
            userRepository.deleteAllByCreatedBy(login);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }


    public void deleteUserEmployees(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            sysServiceProxy.deleteAccountLink(login);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = BadRequestAlertException.class)
	public Object validateOtp(@Valid ValidateOtpVM dto) {
        String resetkey = userRepository.findOneByResetKey(dto.getResetKey())
                .map(user -> {
                    if(user.getLastResendOtp().isBefore(Instant.now().minusSeconds(295))) {
                        throw new BadRequestAlertException(EnumErrors.OTP_EXPIRED);
                    }
                    user.setNumberIncorrectOtp(user.getNumberIncorrectOtp()==null?0: user.getNumberIncorrectOtp());
                    if(user.getNumberIncorrectOtp()>=5) {
                        throw new BadRequestAlertException(EnumErrors.OTP_LIMITED_VALIDATE);
                    }
                    if (!dto.getOtp().equals(user.getOtp())) {
                        user.setNumberIncorrectOtp(user.getNumberIncorrectOtp()==null ?0 : user.getNumberIncorrectOtp()+1);
                        userRepository.saveAndFlush(user);
                        throw new BadRequestAlertException(EnumErrors.OTP_INCORRECT);
                    }
                    user.setResetKey(RandomUtil.generateResetKey());
                    this.clearUserCaches(user);
                    return user.getResetKey();
                }).orElseThrow(() -> new BadRequestAlertException(EnumErrors.RESETKEY_INVALID));
        Map<String, String> map = new HashMap<>();
        map.put("resetKey", resetkey);
        return map;
	}

	public User resendOtpForReset(String resetKey) {
		log.debug("REST Reqeust for send OTP {}", resetKey);
		Optional<User> optUser = userRepository.findOneByResetKey(resetKey);
		if (optUser.isPresent()) {
			return updateOtp(optUser.get(), SMSTemplate.RESTPASSWORD);
		} else {
			throw new BadRequestAlertException(EnumErrors.RESETKEY_INVALID);
		}
	}

	/**
	 * Attach Generate OTP to user and save send otp
	 *
	 * @param user   to save
	 */
	private User updateOtp(User user, SMSTemplate template) {
		// Check limit OTP sent
		Instant now = Instant.now();
		Instant expiredTime = user.getLastResendOtp().plus(Duration.ofDays(1));
		if (user.getLastResendOtp() != null) {
			if (expiredTime.compareTo(now) < 0) {
				user.setNumberSendOtp(0);
			} else {
				if (user.getNumberSendOtp() >= 5) {
					log.debug("Limit opt sent");
					throw new BadRequestAlertException(EnumErrors.LIMIT_OTP);
				}
			}
		}

		// Set new OTP
		user.setNumberSendOtp(user.getNumberSendOtp() + 1);
		user.setLastResendOtp(Instant.now());
		String otp = generateOtp();
		user.setOtp(otp);

		// Send OTP
		log.debug("Process send OTP...");

		//Send active email or otp
        if(ValidatorUtils.validateEmail(user.getLogin())) {
			log.debug("Send email to the user: {}", user.getLogin());
			//TODO send email
		}else if(ValidatorUtils.validatePhone(user.getLogin())) {
			log.debug("Send otp to the phone number: {}", user.getLogin());
            sendOtp(user, template);
        }else {
			throw new BadRequestAlertException(EnumErrors.LOGIN_NAME_INVALID);
		}

		// Save
		User save = userRepository.save(user);
		this.clearUserCaches(user);
		return save;
	}

    private void sendOtp(User user, SMSTemplate template) {
        SmsOtpRequestDTO request = new SmsOtpRequestDTO();
        request.setPhoneNumber(user.getLogin());
        request.setOtp(user.getOtp());
        request.setTemplate(template);
        request.setSecretKey(properties.getSecretKey());
        messageServiceProxy.senSMS(request);
    }

    /**
	 * @return new otp with 6 character
	 */
	private String generateOtp() {

		return String.format("%06d", new Random().nextInt(999999));
	}

	public void checkPhone(String phonenumber) {
		if(!ValidatorUtils.validatePhone(phonenumber)) {
			throw new BadRequestAlertException(EnumErrors.LOGIN_NAME_INVALID);
		}
		if(userRepository.findOneByLogin(phonenumber).filter(User::getActivated).isPresent()) {
			throw new BadRequestAlertException(EnumErrors.EMAIL_ALREADY_USED);
		}
	}
}
