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

/**
 * @author herzigd
 */
public class SimpleBlock {

    private static final int DIFFICULTY = 7;

    private String hash;
    private String previousHash;
    private String data;
    private long nonce;
    private long timestamp;

    public SimpleBlock(String previousHash, String data) {
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String value = previousHash
                + Long.toString(timestamp)
                + Long.toString(nonce)
                + data;
        return CryptoUtil.createHash(value);
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void mineBlock() {
        String target = new String(new char[DIFFICULTY]).replace('\0', '0');
        while (!hash.substring(0, DIFFICULTY).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }
}
