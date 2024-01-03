package com.shop.bike.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "smart_contract")
@Data
public class SmartContract {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "seller_id")
	private String sellerId;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "created_Date")
    private Instant createdDate;

    @Column(name = "buyer_id")
    private String buyerId;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "ethereum_transaction_id")
    private String ethereumTransactionId;

    @Size(max = 50000)
    @Column(name = "content")
    private String content;
}
