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

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author herzigd
 */
public class Wallet {
    
    private String owner;
    private KeyPair keyPair = null;
    public Map<String,TransactionOutput> UTXOs = new HashMap<>();
    
    public Wallet(String owner) {
        this.owner = owner;
        generateKeyPair();
    }
    
    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, random);
            keyPair = keyGen.generateKeyPair();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * The private key is used to sign transactions.
     * @return private key.
     */
    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }
    
    /**
     * The public key is used to receive payments.
     * @return public key.
     */
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public String getOwner() {
        return owner;
    }
    
    public float getBalance() {
        float result = 0.0f;
        
        for (Map.Entry<String, TransactionOutput> item : Chain.UTXOs.entrySet()) {
            TransactionOutput output = item.getValue();
            if (output.isMine(getPublicKey())) {
                UTXOs.put(output.id, output);
                result += output.value;
            }
        }
        return result;
    }
    
    public Transaction sendMoney(PublicKey receiver, float value) {
        if (getBalance() < value) {
            System.out.println("Not enough money for this transaction");
            return null;
        }
        List<TransactionInput> inputs = new ArrayList<>();
        float total = 0.0f;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput output = item.getValue();
            total += output.value;
            inputs.add(new TransactionInput(output.id));
            if (total > value) break;
        }
        Transaction obj = new Transaction(getPublicKey(), receiver, value, inputs);
        obj.generateSignature(getPrivateKey());
        
        for (TransactionInput input : inputs) {
            UTXOs.remove(input.transactionOutputId);
        }
        
        return obj;
    }
}
