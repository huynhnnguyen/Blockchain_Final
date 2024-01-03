package com.shop.bike.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "mining_ethereum")
@Data
public class MiningEthereum {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "user_id")
	private String userId;

    @Column(name = "time_mining")
    private Instant timeMining;

    @Column(name = "total_mining")
    private Double totalMining;

    @Column(name = "total_ethereum_now")
    private Double totalEthereumNow;

    @Column(name = "user_name")
    private String userName;
	
}
