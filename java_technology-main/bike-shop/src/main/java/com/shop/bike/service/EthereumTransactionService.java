package com.shop.bike.service;

import com.shop.bike.admin.dto.EthereumTransactionFilterDTO;
import com.shop.bike.service.dto.EthereumTransactionDTO;
import com.shop.bike.vm.EthereumTransactionVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EthereumTransactionService {

    void createEthereumTransaction(EthereumTransactionDTO ethereumTransactionDTO);

    Page<EthereumTransactionVM> findAllTransaction(EthereumTransactionFilterDTO filterDTO, Pageable pageable);

    EthereumTransactionVM getDetail(Long id);

    void deleteTransaction(Long id);

}
