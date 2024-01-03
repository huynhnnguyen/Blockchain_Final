package com.shop.bike.service.dto.mapper;

import com.shop.bike.entity.EthereumTransaction;
import com.shop.bike.service.EntityMapper;
import com.shop.bike.service.dto.EthereumTransactionDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EthereumTransactionMapper extends EntityMapper<EthereumTransactionDTO, EthereumTransaction> {
}
