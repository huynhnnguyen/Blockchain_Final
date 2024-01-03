package com.shop.bike.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ethereum_price")
@Data
public class EthereumPrice {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "time")
	private Instant time;

    @Column(name = "price")
    private BigDecimal price;
	
}
