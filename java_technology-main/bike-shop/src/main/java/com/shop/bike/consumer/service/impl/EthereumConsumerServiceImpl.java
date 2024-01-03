package com.shop.bike.consumer.service.impl;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.dto.AddressDTO;
import com.shop.bike.consumer.repository.AddressConsumerRepository;
import com.shop.bike.consumer.service.AddressConsumerService;
import com.shop.bike.consumer.service.EthereumConsumerService;
import com.shop.bike.entity.*;
import com.shop.bike.entity.enumeration.ErrorEnum;
import com.shop.bike.service.CountryService;
import com.shop.bike.service.DistrictService;
import com.shop.bike.service.ProvinceService;
import com.shop.bike.service.WardsService;
import com.shop.bike.service.impl.AddressServiceImpl;
import com.shop.bike.service.impl.EthereumServiceImpl;
import com.shop.bike.utils.DistanceUtils;
import com.shop.bike.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Qualifier(ApplicationConstant.CONSUMER)
@Slf4j
public class EthereumConsumerServiceImpl extends EthereumServiceImpl implements EthereumConsumerService {

}
