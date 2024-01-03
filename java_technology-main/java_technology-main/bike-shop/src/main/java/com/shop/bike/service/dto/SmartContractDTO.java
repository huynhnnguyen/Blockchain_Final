package com.shop.bike.service.dto;

import com.shop.bike.entity.enumeration.PaymentGateway;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SmartContractDTO {

	private Long ethereumTransactionId;

    private String content;
}
