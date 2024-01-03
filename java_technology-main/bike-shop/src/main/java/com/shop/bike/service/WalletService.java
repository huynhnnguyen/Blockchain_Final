package com.shop.bike.service;

import com.shop.bike.entity.Wallet;
import com.shop.bike.service.dto.WalletDTO;

public interface WalletService {

    void createWallet(WalletDTO walletDTO);

    void updateWallet(WalletDTO walletDTO);

    Wallet findTotalMoney();

}
