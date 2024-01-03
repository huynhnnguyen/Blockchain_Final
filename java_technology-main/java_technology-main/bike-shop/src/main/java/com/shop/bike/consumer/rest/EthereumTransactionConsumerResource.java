package com.shop.bike.consumer.rest;

import com.shop.bike.admin.dto.EthereumTransactionFilterDTO;
import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.service.EthereumTransactionConsumerService;
import com.shop.bike.security.SecurityUtils;
import com.shop.bike.service.dto.EthereumTransactionDTO;
import com.shop.bike.vm.EthereumTransactionVM;
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
public class EthereumTransactionConsumerResource {
	
	private static final String ENTITY_NAME = "ethereum";
	
	@Autowired
	@Qualifier(ApplicationConstant.CONSUMER)
	private EthereumTransactionConsumerService ethereumTransactionConsumerService;
	
	@PostMapping("/ethereum-transaction")
	public ResponseEntity<Void> createTransactions(@RequestBody EthereumTransactionDTO dto)  {
		log.debug("REST request to create transaction", dto);
        ethereumTransactionConsumerService.createEthereumTransaction(dto);
		return ResponseEntity.noContent().build();
	}


    @PutMapping("/ethereum-transaction/update")
    public ResponseEntity<Void> updateTransactions(@RequestBody EthereumTransactionDTO dto)  {
        log.debug("REST request to create transaction", dto);
        ethereumTransactionConsumerService.createEthereumTransaction(dto);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/ethereum-transactions")
    public ResponseEntity<List<EthereumTransactionVM>> getTransactionHistory(EthereumTransactionFilterDTO filterDTO, Pageable pageable) {
        log.debug("REST request to get shipping fee");
        filterDTO.setSellerId(SecurityUtils.getCurrentUserLogin().get());
        Page<EthereumTransactionVM> page =  ethereumTransactionConsumerService.findAllTransaction(filterDTO,pageable);
        return ResponseEntity.ok(page.getContent());
    }


    @GetMapping("/ethereum-transaction/all")
    public ResponseEntity<List<EthereumTransactionVM>> getTransactionHistoryAll(EthereumTransactionFilterDTO filterDTO, Pageable pageable) {
        log.debug("REST request to get shipping fee");
        filterDTO.setBuyerId(SecurityUtils.getCurrentUserLogin().get());
        Page<EthereumTransactionVM> page =  ethereumTransactionConsumerService.findAllTransaction(filterDTO,pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/ethereum-transactions/{id}")
    public ResponseEntity<EthereumTransactionVM> getTransactionDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get shipping fee");
        EthereumTransactionVM page =  ethereumTransactionConsumerService.getDetail(id);
        return ResponseEntity.ok().body(page);
    }


    @DeleteMapping("/ethereum-transactions/{id}")
    public ResponseEntity<Void> deleteTransactionDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get shipping fee");
        ethereumTransactionConsumerService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
