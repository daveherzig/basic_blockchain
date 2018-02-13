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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author herzigd
 */
public class Transaction {

    private static AtomicInteger sequence = new AtomicInteger();

    public int id;
    public String transactionId;
    public PublicKey sender;
    public PublicKey receiver;
    public float value;
    public byte[] signature;
    public List<TransactionInput> inputs = new ArrayList<>();
    public List<TransactionOutput> outputs = new ArrayList<>();

    public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs) {
        this.id = sequence.incrementAndGet();
        this.sender = from;
        this.receiver = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        return CryptoUtil.createHash(
                ""
                + id
                + CryptoUtil.getStringFromKey(sender)
                + CryptoUtil.getStringFromKey(receiver)
                + value);
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = CryptoUtil.getStringFromKey(sender) + CryptoUtil.getStringFromKey(receiver) + Float.toString(value);
        //System.out.println("Data for generating signature \t" + data);
        signature = CryptoUtil.createSignature(privateKey, data);
    }
    
    public boolean verifySignature() {
        String data = CryptoUtil.getStringFromKey(sender) + CryptoUtil.getStringFromKey(receiver) + Float.toString(value);
        //System.out.println("Data for verify signature\t" + data);
        return CryptoUtil.verifySignature(sender, data, signature);
    }
    
    public boolean processTransaction() {
        if (!verifySignature()) {
            return false;
        }
        
        for (TransactionInput i : inputs) {
            i.UTXO = Chain.UTXOs.get(i.transactionOutputId);
        }
        
        if (getInputsValue() < Chain.MINIMUM_VALUE) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue());
            return false;
        }
        
        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.receiver, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));
        
        for (TransactionOutput output : outputs) {
            Chain.UTXOs.put(output.id, output);
        }
        
        for (TransactionInput input : inputs) {
            if (input.UTXO == null) continue;
            Chain.UTXOs.remove(input.UTXO.id);
        }
        
        return true;
    }
    
    public float getInputsValue() {
        float result = 0.0f;
        for (TransactionInput input : inputs) {
            if (input.UTXO == null) continue;
            result += input.UTXO.value;
        }
        return result;
    }
    
    public float getOutputsValue() {
        float result = 0.0f;
        for (TransactionOutput output : outputs) {
            result += output.value;
        }
        return result;
    }
}
