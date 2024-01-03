package com.shop.bike.consumer.rest;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.service.AddressConsumerService;
import com.shop.bike.entity.EthereumPrice;
import com.shop.bike.service.EthereumPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/consumer")
@Slf4j
public class EthereumPriceConsumerResource {
	
	private static final String ENTITY_NAME = "address";
	
	@Autowired
	private EthereumPriceService ethereumPriceService;
	
	@GetMapping("/ethereum-price/chart")
	public ResponseEntity<List<EthereumPrice>> getShippingFee(@RequestParam(value = "fromDate", required = false) Instant fromDate,
                                                              @RequestParam(value = "toDate", required = false) Instant toDate
															  ) throws URISyntaxException {
		log.debug("REST request to get shipping fee");
		return ResponseEntity.ok(ethereumPriceService.chartEthereum(fromDate,toDate));
	}
}
