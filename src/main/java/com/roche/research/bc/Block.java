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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herzigd
 */
public class Block {

    private String hash;
    private String previousHash;
    private String merkleRoot;
    private List<Transaction> transactions = new ArrayList<>();
    private long timestamp;
    private long nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();
        this.nonce = 0;
    }

    public String calculateHash() {
        String value = previousHash
                + Long.toString(timestamp)
                + Long.toString(nonce)
                + merkleRoot;
        return CryptoUtil.createHash(value);
    }

    public void mineBlock(int difficulty) {
        merkleRoot = CryptoUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0');
        hash = calculateHash();
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }
    
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        if (previousHash != "0") {
            if (!transaction.processTransaction()) {
                System.out.println("Transaction failed to process!");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction successfully added to block");
        return true;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
