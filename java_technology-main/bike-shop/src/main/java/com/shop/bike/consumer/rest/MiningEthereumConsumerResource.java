package com.shop.bike.consumer.rest;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.service.EthereumConsumerService;
import com.shop.bike.consumer.service.MiningEthereumConsumerService;
import com.shop.bike.entity.Ethereum;
import com.shop.bike.entity.MiningEthereum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/api/v1/consumer")
@Slf4j
public class MiningEthereumConsumerResource {
	
	private static final String ENTITY_NAME = "ethereum";
	
	@Autowired
	@Qualifier(ApplicationConstant.CONSUMER)
	private MiningEthereumConsumerService miningEthereumConsumerService;
	
	@PostMapping("/mining-ethereum")
	public ResponseEntity<Void> createMining() {
		log.debug("REST request to get shipping fee");
		miningEthereumConsumerService.miningEthereum();
		return ResponseEntity.noContent().build();
	}

    @GetMapping("/mining-ethereum")
    public ResponseEntity<List<MiningEthereum>> getMiningHistory(Pageable pageable) {
        log.debug("REST request to get shipping fee");
        Page<MiningEthereum> page =  miningEthereumConsumerService.findAllBuUserId(pageable);
        return ResponseEntity.ok(page.getContent());
    }
}
