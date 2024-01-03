package com.shop.bike.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.bike.entity.enumeration.PaymentGateway;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class WalletDTO {

    private Long id;

    private String ownerName;

    private String bankConnect;

    private String bankNumber;

    private PaymentGateway bankType;

    private String idCard;

    private BigDecimal totalAdd;
}
