/*
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

import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author herzigd
 */
public class Chain {

    public static final int DIFFICULTY = 4;
    public static final float MINIMUM_VALUE = 0.1f;
    public static final Map<String, TransactionOutput> UTXOs = new HashMap<>();
    
    private List<Block> data = new ArrayList<>();

    public synchronized void addTransaction(Transaction transaction) {
    }
    
    public synchronized void addBlock(List<Transaction> transactions) {
        Block block = null;
        if (data.size() == 0) {
            block = new Block("0");
        } else {
            block = new Block(data.get(data.size() - 1).getHash());
        }
        for (Transaction t : transactions) {
            block.addTransaction(t);
        }
        block.mineBlock(DIFFICULTY);
        data.add(block);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        for (Block b : data) {
            result.append(b.getHash() + " - " + b.getPreviousHash());
            result.append(System.getProperty("line.separator"));
        }
        return result.toString();
    }

    public String toJSon() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public boolean isValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[DIFFICULTY]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for (int i = 1; i < data.size(); i++) {
            currentBlock = data.get(i);
            previousBlock = data.get(i - 1);

            //compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            //check if hash is solved
            if (!currentBlock.getHash().substring(0, DIFFICULTY).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
