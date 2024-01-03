package com.edenhub.gateway.web.rest;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edenhub.gateway.security.AuthoritiesConstants;
import com.edenhub.gateway.service.dto.AccountLinkDTO;
import com.edenhub.gateway.service.proxy.SysServiceProxy;

@RestController
@RequestMapping("/api/test")
public class TestResource {

    private final Logger log = LoggerFactory.getLogger(TestResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysServiceProxy sysServiceProxy;
    
    public TestResource(SysServiceProxy sysServiceProxy) {
		super();
		this.sysServiceProxy = sysServiceProxy;
	}



	@PostMapping("/accout-links/sys/create")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> createUser() throws URISyntaxException {
		AccountLinkDTO accoutLink = new AccountLinkDTO(1L, "test", "nguyen Tan Luong", "note");
		sysServiceProxy.createSysAccoutLink(accoutLink);
		return ResponseEntity.noContent().build();
    }
}
