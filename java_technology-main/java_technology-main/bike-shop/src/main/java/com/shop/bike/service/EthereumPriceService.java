package com.shop.bike.service;

import com.shop.bike.entity.EthereumPrice;
import com.shop.bike.entity.Wallet;
import com.shop.bike.service.dto.WalletDTO;

import java.time.Instant;
import java.util.List;

public interface EthereumPriceService {

    List<EthereumPrice> chartEthereum(Instant fromDate, Instant toDate);

}
