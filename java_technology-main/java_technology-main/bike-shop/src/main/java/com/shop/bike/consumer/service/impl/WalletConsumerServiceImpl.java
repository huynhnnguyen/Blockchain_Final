package com.shop.bike.consumer.service.impl;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.service.SmartContractConsumerService;
import com.shop.bike.consumer.service.WalletConsumerService;
import com.shop.bike.service.impl.SmartContractServiceImpl;
import com.shop.bike.service.impl.WalletServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Qualifier(ApplicationConstant.CONSUMER)
@Slf4j
public class WalletConsumerServiceImpl extends WalletServiceImpl implements WalletConsumerService {

}
