package com.shop.bike.consumer.rest;

import com.shop.bike.admin.dto.EthereumTransactionFilterDTO;
import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.service.EthereumTransactionConsumerService;
import com.shop.bike.consumer.service.SmartContractConsumerService;
import com.shop.bike.entity.SmartContract;
import com.shop.bike.security.SecurityUtils;
import com.shop.bike.service.dto.EthereumTransactionDTO;
import com.shop.bike.service.dto.SmartContractDTO;
import com.shop.bike.vm.EthereumTransactionVM;
import com.shop.bike.vm.SmartContractVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/consumer")
@Slf4j
public class SmartContractConsumerResource {
	
	private static final String ENTITY_NAME = "ethereum";
	
	@Autowired
	@Qualifier(ApplicationConstant.CONSUMER)
	private SmartContractConsumerService smartContractConsumerService;
	
	@PostMapping("/smart-contract")
	public ResponseEntity<Void> createSmartContract(@RequestBody SmartContractDTO dto)  {
		log.debug("REST request to create transaction", dto);
        smartContractConsumerService.createSmartContract(dto);
		return ResponseEntity.noContent().build();
	}

    @GetMapping("/smart-contract")
    public ResponseEntity<List<SmartContractVM>> getListMySmartContract(Pageable pageable)  {
        log.debug("REST request to get smart contract");
        Page<SmartContractVM> page = smartContractConsumerService.findAllByMySmartContract(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/smart-contract/{id}")
    public ResponseEntity<SmartContractVM> getListMySmartContract(@PathVariable("id") Long id)  {
        log.debug("REST request to get smart contract");
        SmartContractVM smartContractVM = smartContractConsumerService.getDetailSmartContract(id);
        return ResponseEntity.ok(smartContractVM);
    }
}
