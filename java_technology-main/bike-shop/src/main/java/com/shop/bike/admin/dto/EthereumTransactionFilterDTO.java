package com.shop.bike.admin.dto;

import com.shop.bike.entity.enumeration.OrderStatus;
import lombok.Data;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class EthereumTransactionFilterDTO {

    private String keyword;

    private String code;

    private String email;

    private String sellerName;

    private Instant fromDate;

    private Instant toDate;

    private Instant lastModifiedDateFrom;

    private Instant lastModifiedDateTo;

    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    private Integer quantityEthFrom;

    private Integer quantityEthTo;

    private String sellerId;

    private String buyerId;
}
