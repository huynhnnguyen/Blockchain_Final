package com.shop.bike.entity;

import com.shop.bike.entity.enumeration.ActionStatus;
import com.shop.bike.entity.enumeration.MediaType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ethereum")
@Data
public class Ethereum {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "total_ether")
	private Double totalEther;
	
}
