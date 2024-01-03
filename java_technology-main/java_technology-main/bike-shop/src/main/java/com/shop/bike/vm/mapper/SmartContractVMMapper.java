package com.shop.bike.vm.mapper;


import com.shop.bike.entity.Country;
import com.shop.bike.entity.EthereumTransaction;
import com.shop.bike.entity.SmartContract;
import com.shop.bike.service.EntityMapper;
import com.shop.bike.vm.CountryVM;
import com.shop.bike.vm.EthereumTransactionVM;
import com.shop.bike.vm.SmartContractVM;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Country} and its VM {@link CountryVM}.
 */
@Mapper(componentModel = "spring")
public interface SmartContractVMMapper extends EntityMapper<SmartContractVM, SmartContract> {
	
}
