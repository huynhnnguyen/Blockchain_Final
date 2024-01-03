package com.shop.bike.service;

import com.shop.bike.consumer.dto.StatisticEthereumDTO;
import com.shop.bike.entity.Ethereum;

public interface EthereumService {

    StatisticEthereumDTO findTotal();

}
