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
import java.util.Random;

/**
 *
 * @author herzigd
 */
public class SimpleChain {

    private List<SimpleBlock> bc = new ArrayList<>();

    public void addBlock(String data) {
        SimpleBlock block;
        if (bc.isEmpty()) {
            block = new SimpleBlock(null, data);
        } else {
            block = new SimpleBlock(bc.get(bc.size() - 1).calculateHash(), data);
        }
        //block.mineBlock();
        bc.add(block);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        for (SimpleBlock block : bc) {
            result.append(block.getData());
            result.append(", PrevHash: ");
            result.append(block.getPreviousHash());
            result.append(", Hash:");
            result.append(block.getHash());
            result.append("\n");
        }
        return result.toString();
    }

    public boolean validate() {
        SimpleBlock currentBlock;
        SimpleBlock previousBlock;

        //loop through blockchain to check hashes:
        for (int i = 1; i < bc.size(); i++) {
            currentBlock = bc.get(i);
            previousBlock = bc.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                return false;
            }
        }
        return true;
    }
    
    public void securityAttack() {
        // change data in a block
        Random random = new Random();
        int index = random.nextInt(bc.size());
        bc.get(index).setData("CRAZY HACK!");
    }
}
