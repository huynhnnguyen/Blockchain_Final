package com.shop.bike.vm;

import com.shop.bike.entity.enumeration.PaymentGateway;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class SmartContractVM {

    private Long id;

    private String sellerId;

    private String sellerName;

    private Instant createdDate;

    private String buyerId;

    private String buyerName;

    private BigDecimal price;

    private Integer quantityEth;
}
