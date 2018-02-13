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

/**
 *
 * @author herzigd
 */
public class Main {
    
    public static void main(String [] args) {
        
        // setup security provide
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        // create new wallets
        Wallet gilsdom1 = new Wallet("Moritz Gilsdorf");
        Wallet doerneng = new Wallet("Gunther Doernen");
        Wallet herzigd = new Wallet("David Herzig");
        
        // show the keys
        System.out.println(CryptoUtil.getStringFromKey(gilsdom1.getPrivateKey()));
        System.out.println(CryptoUtil.getStringFromKey(gilsdom1.getPublicKey()));
        
        System.out.println(CryptoUtil.getStringFromKey(doerneng.getPrivateKey()));
        System.out.println(CryptoUtil.getStringFromKey(doerneng.getPublicKey()));
        
        System.out.println(CryptoUtil.getStringFromKey(herzigd.getPrivateKey()));
        System.out.println(CryptoUtil.getStringFromKey(herzigd.getPublicKey()));
        
        // moritz transfers some money to gunther
        Transaction transaction = new Transaction(gilsdom1.getPublicKey(), doerneng.getPublicKey(), 5, null);
        transaction.generateSignature(gilsdom1.getPrivateKey());
        
        // verify signature
        System.out.println(transaction.verifySignature());
        
        
    }
    
}
