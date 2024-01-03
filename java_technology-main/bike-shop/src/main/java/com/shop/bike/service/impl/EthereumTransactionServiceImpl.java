package com.shop.bike.service.impl;

import com.shop.bike.admin.dto.EthereumTransactionFilterDTO;
import com.shop.bike.entity.EthereumTransaction;
import com.shop.bike.entity.User;
import com.shop.bike.entity.Wallet;
import com.shop.bike.entity.enumeration.ErrorEnum;
import com.shop.bike.repository.*;
import com.shop.bike.security.SecurityUtils;
import com.shop.bike.service.EthereumTransactionService;
import com.shop.bike.service.dto.EthereumTransactionDTO;
import com.shop.bike.service.dto.mapper.EthereumTransactionMapper;
import com.shop.bike.vm.EthereumTransactionVM;
import com.shop.bike.vm.mapper.EthereumTransactionVMMapper;
import com.shop.bike.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@Primary
public class EthereumTransactionServiceImpl implements EthereumTransactionService {

    @Autowired
    private EthereumTransactionRepository ethereumTransactionRepository;

    @Autowired
    private EthereumTransactionMapper ethereumTransactionMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private MiningEthereumRepository miningEthereumRepository;

    @Autowired
    private EthereumTransactionVMMapper vmMapper;

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public void createEthereumTransaction(EthereumTransactionDTO ethereumTransactionDTO) {
        EthereumTransaction ethereumTransaction;
        User user = userRepository.findById(Long.parseLong(SecurityUtils.getCurrentUserLogin().get()))
                .orElseThrow(()-> new BadRequestAlertException(ErrorEnum.USER_NOT_FOUND));

        Wallet wallet = walletRepository.findByOwnerId(user.getId().toString());
        if(wallet == null) {
            throw new RuntimeException("You must create wallet first");
        }
        Random random = new Random();
        Integer totalEthereumOfUser = miningEthereumRepository.countByUserId(user.getId().toString());
        if (totalEthereumOfUser < ethereumTransactionDTO.getQuantityEth()) {
            throw new BadRequestAlertException("Not","Enough","you are not enough ethereum");
        }
        if(ethereumTransactionDTO.getId()==null) {
            ethereumTransaction = ethereumTransactionMapper.toEntity(ethereumTransactionDTO);
            ethereumTransaction.setDate(Instant.now());
            ethereumTransaction.setLastModifiedDate(Instant.now());
            ethereumTransaction.setCode("ETH_"+ random.nextInt(10000000));
            ethereumTransaction.setSellerId(user.getId());
            ethereumTransaction.setSellerName(user.getName());
            ethereumTransaction.setEmail(user.getEmail());
        }
        else {

            ethereumTransaction = ethereumTransactionRepository.findById(ethereumTransactionDTO.getId())
                            .orElseThrow(()-> new BadRequestAlertException(ErrorEnum.TRANSACTION_NOT_FOUND));
            if(!user.getId().equals(ethereumTransaction.getSellerId())) {
                throw new BadRequestAlertException("Transaction","can't update","Transaction can't update by not owner");
            }
            ethereumTransaction.setLastModifiedDate(Instant.now());
            ethereumTransactionMapper.partialUpdate(ethereumTransaction,ethereumTransactionDTO);
        }
        ethereumTransactionRepository.save(ethereumTransaction);
    }

    @Override
    public Page<EthereumTransactionVM> findAllTransaction(EthereumTransactionFilterDTO filterDTO, Pageable pageable) {
        return ethereumTransactionRepository.findAllTransactions(filterDTO.getKeyword(),
                filterDTO.getCode(),
                filterDTO.getEmail(),
                filterDTO.getSellerName(),
                filterDTO.getFromDate(),
                filterDTO.getToDate(),
                filterDTO.getLastModifiedDateFrom(),
                filterDTO.getLastModifiedDateTo(),
                filterDTO.getPriceFrom(),
                filterDTO.getPriceTo(),
                filterDTO.getQuantityEthFrom(),
                filterDTO.getQuantityEthTo(),
                filterDTO.getSellerId(),
                filterDTO.getBuyerId(),
                pageable).map(vmMapper::toDto);
    }

    @Override
    public EthereumTransactionVM getDetail(Long id) {
        return vmMapper.toDto(ethereumTransactionRepository.findById(id)
                .orElseThrow(()-> new BadRequestAlertException("Transaction","notFound","Transaction are not found")));
    }

    @Override
    public void deleteTransaction(Long id) {
        ethereumTransactionRepository.deleteById(id);
    }
}
