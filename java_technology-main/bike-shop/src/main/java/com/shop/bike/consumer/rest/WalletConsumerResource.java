package com.shop.bike.consumer.rest;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.service.SmartContractConsumerService;
import com.shop.bike.consumer.service.WalletConsumerService;
import com.shop.bike.entity.Wallet;
import com.shop.bike.service.dto.SmartContractDTO;
import com.shop.bike.service.dto.WalletDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/consumer")
@Slf4j
public class WalletConsumerResource {
	
	private static final String ENTITY_NAME = "ethereum";
	
	@Autowired
	@Qualifier(ApplicationConstant.CONSUMER)
	private WalletConsumerService walletConsumerService;
	
	@PostMapping("/wallet/create")
	public ResponseEntity<Void> createTransactions(@RequestBody WalletDTO dto)  {
		log.debug("REST request to create transaction", dto);
        walletConsumerService.createWallet(dto);
		return ResponseEntity.noContent().build();
	}

    @PutMapping("/wallet/update")
    public ResponseEntity<Void> updateWallet(@RequestBody WalletDTO dto)  {
        log.debug("REST request to create transaction", dto);
        walletConsumerService.updateWallet(dto);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/wallet/my-wallet")
    public ResponseEntity<Wallet> getMyWallet() {
        return ResponseEntity.ok(walletConsumerService.findTotalMoney());
    }

    @GetMapping("/wallet/my-money")
    public ResponseEntity<Map<String, BigDecimal>> getMyTotalMoney() {
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("totalMoney", walletConsumerService.findTotalMoney().getTotalMoney());
        return ResponseEntity.ok(map);
    }
}
