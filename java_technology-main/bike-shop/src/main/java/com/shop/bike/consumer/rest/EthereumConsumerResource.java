package com.shop.bike.consumer.rest;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.dto.StatisticEthereumDTO;
import com.shop.bike.consumer.service.AddressConsumerService;
import com.shop.bike.consumer.service.EthereumConsumerService;
import com.shop.bike.entity.Ethereum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URISyntaxException;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/consumer")
@Slf4j
public class EthereumConsumerResource {
	
	private static final String ENTITY_NAME = "ethereum";
	
	@Autowired
	@Qualifier(ApplicationConstant.CONSUMER)
	private EthereumConsumerService ethereumConsumerService;
	
	@GetMapping("/ethereum/total")
	public ResponseEntity<StatisticEthereumDTO> getShippingFee() throws URISyntaxException {
		log.debug("REST request to get shipping fee");
        StatisticEthereumDTO result = ethereumConsumerService.findTotal();
		return ResponseEntity.ok(result);
	}
}
