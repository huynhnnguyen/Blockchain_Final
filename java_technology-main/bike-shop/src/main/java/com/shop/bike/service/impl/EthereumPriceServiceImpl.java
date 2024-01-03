package com.shop.bike.service.impl;

import com.shop.bike.entity.EthereumPrice;
import com.shop.bike.repository.EthereumPriceRepository;
import com.shop.bike.service.AddressService;
import com.shop.bike.service.EthereumPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@Primary
public class EthereumPriceServiceImpl implements EthereumPriceService {


    @Autowired
    private EthereumPriceRepository ethereumPriceRepository;


    @Override
    public List<EthereumPrice> chartEthereum(Instant fromDate, Instant toDate) {
        return ethereumPriceRepository.ethereumPriceChart(fromDate, toDate);
    }
}
