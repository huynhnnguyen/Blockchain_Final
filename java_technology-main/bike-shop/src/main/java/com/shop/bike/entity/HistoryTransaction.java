package com.shop.bike.entity;

import com.shop.bike.entity.enumeration.OrderStatus;
import com.shop.bike.entity.enumeration.PaymentGateway;
import com.shop.bike.entity.enumeration.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;


/**
 * A MyOrder.
 */
@Entity
@Table(name = "history_transaction")
@Getter
@Setter
public class HistoryTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;


    @Column(name = "buyer_id")
    private Long buyerId;

    @Column(name = "buyer_name")
    private String buyerName;
    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "smart_contract_id")
    private Long smartContractId;

    @Column(name = "quantity_eth")
    private Integer quantityEth;

    @Column(name = "price")
    private BigDecimal price;

}
