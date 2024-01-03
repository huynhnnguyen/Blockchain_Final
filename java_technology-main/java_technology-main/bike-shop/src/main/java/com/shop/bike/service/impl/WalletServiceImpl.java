package com.shop.bike.service.impl;

import com.shop.bike.entity.EthereumTransaction;
import com.shop.bike.entity.SmartContract;
import com.shop.bike.entity.User;
import com.shop.bike.entity.Wallet;
import com.shop.bike.entity.enumeration.ErrorEnum;
import com.shop.bike.repository.EthereumTransactionRepository;
import com.shop.bike.repository.SmartContractRepository;
import com.shop.bike.repository.UserRepository;
import com.shop.bike.repository.WalletRepository;
import com.shop.bike.security.SecurityUtils;
import com.shop.bike.service.SmartContractService;
import com.shop.bike.service.WalletService;
import com.shop.bike.service.dto.SmartContractDTO;
import com.shop.bike.service.dto.WalletDTO;
import com.shop.bike.service.dto.mapper.WalletMapper;
import com.shop.bike.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;

@Service
@Transactional
@Primary
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletMapper walletMapper;

    private static final SecureRandom SECURE_RANDOM;

    static {
        SECURE_RANDOM = new SecureRandom();
        SECURE_RANDOM.nextBytes(new byte[64]);
    }



    @Override
    public void createWallet(WalletDTO walletDTO) {
        Wallet wallet;
        if(walletDTO.getId() == null) {
            wallet = walletMapper.toEntity(walletDTO);
            wallet.setOwnerId(SecurityUtils.getCurrentUserLogin().get());
            wallet.setCodeWallet("WALLET"+ generateRandomAlphanumericString());
            wallet.setCode("CODE"+ generateRandomAlphanumericString());
            wallet.setCreatedDate(Instant.now());
            BigDecimal totalMoney = new BigDecimal("0");
            wallet.setTotalMoney(totalMoney);
        }
        else {
            wallet = walletRepository.findById(walletDTO.getId())
                    .orElseThrow(()-> new BadRequestAlertException("Wallet","NotFound","Wallet not Found"));
            walletMapper.partialUpdate(wallet,walletDTO);
            if(walletDTO.getTotalAdd()!=null){
                wallet.setTotalMoney(wallet.getTotalMoney().add(walletDTO.getTotalAdd()));
            }
        }
        walletRepository.save(wallet);
    }

    @Override
    public void updateWallet(WalletDTO walletDTO) {
        Wallet wallet = walletRepository.findByOwnerId(SecurityUtils.getCurrentUserLogin().get());
        if(wallet == null) {
            throw new RuntimeException("You don't have wallet");
        }
        walletMapper.partialUpdate(wallet,walletDTO);
        if(walletDTO.getTotalAdd()!=null){
            wallet.setTotalMoney(wallet.getTotalMoney().add(walletDTO.getTotalAdd()));
        }
        walletRepository.save(wallet);
    }

    @Override
    public Wallet findTotalMoney() {
        return walletRepository.findByOwnerId(SecurityUtils.getCurrentUserLogin().get());
    }




    public static String generateRandomAlphanumericString() {
        return RandomStringUtils.random(20, 0, 0, true, true, null, SECURE_RANDOM);
    }
}
