/*
 * Copyright (C) 2018 David Herzig (dave.herzig@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.roche.research.bc;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herzigd
 */
public class Main {
    
    public static void main(String [] args) {
        
        // setup security provide
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        // create blockchain
        Chain blockChain = new Chain();
        
        // create new wallets
        Wallet gilsdom1 = new Wallet("Moritz Gilsdorf");
        Wallet doerneng = new Wallet("Gunther Doernen");
        Wallet herzigd = new Wallet("David Herzig");
        List<Wallet> wallets = new ArrayList<>();
        wallets.add(gilsdom1);
        wallets.add(doerneng);
        wallets.add(herzigd);
        
        // show the keys
        System.out.println("Keys for Moritz");
        System.out.println(CryptoUtil.getStringFromKey(gilsdom1.getPrivateKey()));
        System.out.println(CryptoUtil.getStringFromKey(gilsdom1.getPublicKey()));
        
        System.out.println("Keys for Gunther");
        System.out.println(CryptoUtil.getStringFromKey(doerneng.getPrivateKey()));
        System.out.println(CryptoUtil.getStringFromKey(doerneng.getPublicKey()));
        
        System.out.println("Keys for David");
        System.out.println(CryptoUtil.getStringFromKey(herzigd.getPrivateKey()));
        System.out.println(CryptoUtil.getStringFromKey(herzigd.getPublicKey()));
        
        // create some transactions - give moritz some coins
        Wallet coinbase = new Wallet("init");
        Transaction initTxGilsdom1 = new Transaction(coinbase.getPublicKey(), gilsdom1.getPublicKey(), 100.0f, null);
        initTxGilsdom1.generateSignature(coinbase.getPrivateKey());
        initTxGilsdom1.transactionId = "0";
        initTxGilsdom1.outputs.add(new TransactionOutput(initTxGilsdom1.receiver, initTxGilsdom1.value, initTxGilsdom1.transactionId));
        Chain.UTXOs.put(initTxGilsdom1.outputs.get(0).id, initTxGilsdom1.outputs.get(0));
        blockChain.addBlock(initTxGilsdom1);
        
        printBalance(wallets);
        
        // transfer some money
        Transaction m2gTransaction = gilsdom1.sendMoney(doerneng.getPublicKey(), 20);
        blockChain.addBlock(m2gTransaction);
        
        printBalance(wallets);
        
        System.out.println(blockChain.toJSon());
    }
    
    private static void printBalance(List<Wallet> wallets) {
        for (Wallet wallet : wallets) {
            System.out.println(wallet.getOwner() + " : " + wallet.getBalance());
        }
    }
    
}
