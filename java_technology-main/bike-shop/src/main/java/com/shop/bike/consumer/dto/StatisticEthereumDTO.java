package com.shop.bike.consumer.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class StatisticEthereumDTO implements Serializable {

    private Double totalEthereum;

    private Double totalEthereumMining;

    private Integer totalTrading;

    private Integer totalCustomer;
}
