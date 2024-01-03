package com.shop.bike.vm;

import com.shop.bike.entity.enumeration.PaymentGateway;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class EthereumTransactionVM {



    private Long id;

    private String code;

    private PaymentGateway paymentGateway;

    private Integer quantityEth;

    private String note;

    private BigDecimal price;

    private String sellerName;

    private String email;

    private Instant date;

    private Instant LastModifiedDate;
}
