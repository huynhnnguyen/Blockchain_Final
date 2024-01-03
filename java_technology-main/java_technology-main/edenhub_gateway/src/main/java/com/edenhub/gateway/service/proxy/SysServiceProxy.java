package com.edenhub.gateway.service.proxy;

import com.edenhub.gateway.service.dto.ProfileAccountPayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edenhub.gateway.service.dto.AccountLinkDTO;

@FeignClient(name = "edenhub-services")
public interface SysServiceProxy {

	@PostMapping(value = "/api/v1/account-links/sys", produces = "application/json")
	void createSysAccoutLink(@RequestBody AccountLinkDTO accoutLink);
	
	@PostMapping(value = "/api/v1/account-links/log-time", produces = "application/json")
	void logLoginTime(@RequestHeader("Authorization") String authHeader);

	@PostMapping(value = "/api/v1/account-links/log-time", produces = "application/json")
	void logLoginTime();

    @PostMapping(value = "/api/v1/sys-accounts/employees/profile", produces = "application/json")
    ResponseEntity<Void> createSysEmployeesAccount(@RequestBody ProfileAccountPayload profileAccountPayload);

    @DeleteMapping("/api/v1/account-links/user-name")
    ResponseEntity<Void> deleteAccountLink(@RequestParam(name = "userName") String userName);

}
