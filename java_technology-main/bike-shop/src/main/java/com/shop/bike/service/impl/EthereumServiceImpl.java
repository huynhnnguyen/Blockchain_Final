package com.shop.bike.service.impl;

import com.shop.bike.consumer.dto.StatisticEthereumDTO;
import com.shop.bike.entity.Ethereum;
import com.shop.bike.entity.MyEthereum;
import com.shop.bike.entity.enumeration.ErrorEnum;
import com.shop.bike.repository.EthereumRepository;
import com.shop.bike.repository.MiningEthereumRepository;
import com.shop.bike.repository.MyEthereumRepository;
import com.shop.bike.security.SecurityUtils;
import com.shop.bike.service.AddressService;
import com.shop.bike.service.EthereumService;
import com.shop.bike.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Primary
public class EthereumServiceImpl implements EthereumService {

    @Autowired
    private EthereumRepository repository;

    @Autowired
    private MiningEthereumRepository miningEthereumRepository;

    @Autowired
    private MyEthereumRepository myEthereumRepository;


    @Override
    public StatisticEthereumDTO findTotal() {
        String userId = SecurityUtils.getCurrentUserLogin().get();
        StatisticEthereumDTO statisticEthereumDTO = new StatisticEthereumDTO();
        statisticEthereumDTO.setTotalEthereum(myEthereumRepository.findByUserId(userId)
                .orElseThrow(()->new BadRequestAlertException(ErrorEnum.USER_NOT_FOUND)).getTotalEther());
        statisticEthereumDTO.setTotalEthereumMining(Double.parseDouble(miningEthereumRepository.countByUserId(userId).toString()));
        statisticEthereumDTO.setTotalTrading(miningEthereumRepository.countTotalTransactionByUserId(userId));
        statisticEthereumDTO.setTotalCustomer(miningEthereumRepository.countTotalCustomerByUserId(userId));
        return statisticEthereumDTO;
    }
}
