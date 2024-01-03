package com.shop.bike.consumer.repository;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.entity.EthereumTransaction;
import com.shop.bike.repository.EthereumTransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier(ApplicationConstant.CONSUMER)
public interface MyOrderConsumerRepository extends EthereumTransactionRepository {

	List<EthereumTransaction> findAllByBuyerId(Long buyerId);
}
