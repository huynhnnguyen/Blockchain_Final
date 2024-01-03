package com.shop.bike.service.impl;

import com.shop.bike.consumer.dto.StatisticEthereumDTO;
import com.shop.bike.entity.*;
import com.shop.bike.entity.enumeration.ErrorEnum;
import com.shop.bike.repository.*;
import com.shop.bike.security.SecurityUtils;
import com.shop.bike.service.EthereumService;
import com.shop.bike.service.SmartContractService;
import com.shop.bike.service.dto.SmartContractDTO;
import com.shop.bike.vm.SmartContractVM;
import com.shop.bike.vm.mapper.SmartContractVMMapper;
import com.shop.bike.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
@Primary
public class SmartContractServiceImpl implements SmartContractService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmartContractRepository smartContractRepository;

    @Autowired
    private SmartContractVMMapper smartContractVMMapper;

    @Autowired
    private EthereumTransactionRepository ethereumTransactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private HistoryTransactionRepository historyTransactionRepository;

    @Autowired
    private MyEthereumRepository myEthereumRepository;


    @Override
    public void createSmartContract(SmartContractDTO smartContractDTO) {
        User user = userRepository.findById(Long.parseLong(SecurityUtils.getCurrentUserLogin().get()))
                .orElseThrow(()-> new BadRequestAlertException(ErrorEnum.USER_NOT_FOUND));
        EthereumTransaction ethereumTransaction = ethereumTransactionRepository.findById(smartContractDTO.getEthereumTransactionId())

                .orElseThrow(()-> new BadRequestAlertException("TRANSACTION","NOTFOUND","TRANSACTION NOT FOUND"));
        Integer totalTrading = ethereumTransaction.getQuantityEth();
        BigDecimal price = ethereumTransaction.getPrice();

        //update quantity Ethereum of seller
        Optional<MyEthereum> myEthereumOfSeller = myEthereumRepository.findByUserId(ethereumTransaction.getSellerId().toString());
        myEthereumOfSeller.get().setTotalEther(myEthereumOfSeller.get().getTotalEther() - totalTrading);
        myEthereumRepository.save(myEthereumOfSeller.get());

        //find wallet of buyer
        Wallet walletBuyer = walletRepository.findByOwnerId(user.getId().toString());

        //find wallet of seller
        Wallet walletSeller = walletRepository.findByOwnerId(ethereumTransaction.getSellerId().toString());
        if(ethereumTransaction.getPrice().compareTo(walletBuyer.getTotalMoney()) > 0) {
            throw new BadRequestAlertException("Money","Not enough","you are not enough money to buy this eth");
        }
        SmartContract smartContract = new SmartContract();
        smartContract.setBuyerId(user.getId().toString());
        smartContract.setBuyerName(user.getName());
        smartContract.setSellerId(ethereumTransaction.getSellerId().toString());
        smartContract.setSellerName(ethereumTransaction.getSellerName());
        smartContract.setCreatedDate(Instant.now());
        smartContract.setEthereumTransactionId(smartContractDTO.getEthereumTransactionId().toString());
        smartContract.setContent(smartContractDTO.getContent());
        ethereumTransactionRepository.deleteById(smartContractDTO.getEthereumTransactionId());

        //set wallet sub money form buyer
        walletBuyer.setTotalMoney(walletBuyer.getTotalMoney().subtract(ethereumTransaction.getPrice()));
        walletRepository.save(walletBuyer);

        //add money for seller
        walletSeller.setTotalMoney(walletSeller.getTotalMoney().add(ethereumTransaction.getPrice()));
        walletRepository.save(walletSeller);

        //save smart contract
        smartContractRepository.save(smartContract);

        // create history transaction
        HistoryTransaction historyTransaction = new HistoryTransaction();
        historyTransaction.setSmartContractId(smartContract.getId());
        historyTransaction.setBuyerId(user.getId());
        historyTransaction.setBuyerName(user.getName());
        historyTransaction.setSellerId(ethereumTransaction.getSellerId());
        historyTransaction.setSellerName(ethereumTransaction.getSellerName());
        historyTransaction.setQuantityEth(totalTrading);
        historyTransaction.setPrice(price);
        historyTransactionRepository.save(historyTransaction);


        Optional<MyEthereum> myEthereumOfBuyer  = myEthereumRepository.findByUserId(user.getId().toString());
        if (myEthereumOfBuyer.isPresent()) {
            myEthereumOfBuyer.get().setTotalEther(myEthereumOfBuyer.get().getTotalEther()+ totalTrading);
            myEthereumRepository.save(myEthereumOfBuyer.get());
        }
        else {
            MyEthereum myEthereum = new MyEthereum();
            myEthereum.setTotalEther(Double.parseDouble(totalTrading+".0"));
            myEthereum.setUserId(SecurityUtils.getCurrentUserLogin().get());
            myEthereumRepository.save(myEthereum);
        }
    }

    @Override
    public Page<SmartContractVM> findAllByMySmartContract(Pageable pageable) {
        return smartContractRepository.findAllBySmartContract(SecurityUtils.getCurrentUserLogin().get(), pageable)
                .map(smartContract -> {
                    SmartContractVM smartContractVM = smartContractVMMapper.toDto(smartContract);
                    HistoryTransaction historyTransaction = historyTransactionRepository.findBySmartContractId(smartContract.getId());
                    smartContractVM.setPrice(historyTransaction.getPrice());
                    smartContractVM.setQuantityEth(historyTransaction.getQuantityEth());
                    return smartContractVM;
                }
        );
    }

    @Override
    public SmartContractVM getDetailSmartContract(Long id) {
        SmartContract smartContract = smartContractRepository.findById(id).orElseThrow(() ->
                new BadRequestAlertException("SmartContract","NotFound","SmartContract NotFound"));
        SmartContractVM smartContractVM = smartContractVMMapper.toDto(smartContract);
        HistoryTransaction historyTransaction = historyTransactionRepository.findBySmartContractId(id);
        smartContractVM.setPrice(historyTransaction.getPrice());
        smartContractVM.setQuantityEth(historyTransaction.getQuantityEth());
        return smartContractVM;
    }


}
