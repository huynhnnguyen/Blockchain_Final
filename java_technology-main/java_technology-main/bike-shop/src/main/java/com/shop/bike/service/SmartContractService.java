package com.shop.bike.service;

import com.shop.bike.admin.dto.EthereumTransactionFilterDTO;
import com.shop.bike.entity.SmartContract;
import com.shop.bike.service.dto.EthereumTransactionDTO;
import com.shop.bike.service.dto.SmartContractDTO;
import com.shop.bike.vm.EthereumTransactionVM;
import com.shop.bike.vm.SmartContractVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SmartContractService {

    void createSmartContract(SmartContractDTO smartContractDTO);

    Page<SmartContractVM> findAllByMySmartContract(Pageable pageable);

    SmartContractVM getDetailSmartContract(Long id);

}
