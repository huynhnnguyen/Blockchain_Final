package com.shop.bike.consumer.service.impl;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.service.EthereumConsumerService;
import com.shop.bike.consumer.service.SmartContractConsumerService;
import com.shop.bike.service.impl.EthereumServiceImpl;
import com.shop.bike.service.impl.SmartContractServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Qualifier(ApplicationConstant.CONSUMER)
@Slf4j
public class SmartContractConsumerServiceImpl extends SmartContractServiceImpl implements SmartContractConsumerService {

}
