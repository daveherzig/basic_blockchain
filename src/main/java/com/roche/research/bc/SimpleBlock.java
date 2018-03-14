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
    
    private String hash;
    private String previousHash;
    private String data;
    private long nonce;
    private long timestamp;
    
    public SimpleBlock(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();
        this.nonce = 0;
    }
    
    public String calculateHash() {
        String value = previousHash
                + Long.toString(timestamp)
                + Long.toString(nonce)
                + data;
        return CryptoUtil.createHash(value);
    }
    
    public static void main(String [] args) {
        SimpleBlock b1 = new SimpleBlock(null);
        SimpleBlock b2 = new SimpleBlock(b1.calculateHash());
        SimpleBlock b3 = new SimpleBlock(b2.calculateHash());
    }
}
