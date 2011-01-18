/**
 *
 * Copyright (c) Ericsson AB, 2010.
 *
 * All Rights Reserved. Reproduction in whole or in part is prohibited
 * without the written consent of the copyright owner.
 *
 * ERICSSON MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. ERICSSON SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * User: lmcnise
 * Date: 1/13/11
 * Time: 2:26 PM
 */
package com.ericsson.edible.fasttrack.dao;

import com.ericsson.edible.fasttrack.TreeBuilder;
import com.ericsson.edible.fasttrack.object.HNode;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class TreeBuilderTest {

    @Test
    public void dummy() {
        System.out.println("dummy");
    }

    public static TreeBuilder setup() {
        TreeBuilder tb = new TreeBuilder("mine");

        List<String> names = new LinkedList<String>();
        /* for(int i = 0; i< 3; i++) {
            for(int j = 0; j < 3; j++){
                names.add(String.valueOf(i) + j);
            }
        }*/
        names.add("11");
        names.add("12");
        names.add("13");

        tb.addChildren(tb.getHead(), names);


        for (HNode lvl : tb.getHead().children) {
            names.clear();
            names.add(lvl.name + "1");
            names.add(lvl.name + "2");
            tb.addChildren(lvl, names);
        }

        return tb;
    }
}
