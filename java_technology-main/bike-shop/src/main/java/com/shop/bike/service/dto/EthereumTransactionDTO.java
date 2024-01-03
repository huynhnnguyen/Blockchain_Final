package com.shop.bike.service.dto;

import com.shop.bike.entity.enumeration.PaymentGateway;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EthereumTransactionDTO {

	private Long id;

    private Integer quantityEth;

    private BigDecimal price;

    private PaymentGateway paymentGateway;
	
	private String email;

    private String note;
}
