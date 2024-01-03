package com.shop.bike.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.bike.entity.enumeration.PaymentGateway;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "wallet")
@Data
public class Wallet {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "owner_id")
    @Unique
	private String ownerId;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "total_money")
    private BigDecimal totalMoney;

    @Column(name = "created_Date")
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();

    @Column(name = "code")
    private String code;

    @Column(name = "bank_connect")
    private String bankConnect;

    @Column(name = "bank_number")
    private String bankNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_type")
    private PaymentGateway bankType;

    @Column(name = "code_wallet")
    private String codeWallet;

    @Column(name = "id_card")
    private String idCard;

}
