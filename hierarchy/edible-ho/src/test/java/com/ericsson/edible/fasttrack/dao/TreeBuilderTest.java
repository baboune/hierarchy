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
    public void testBuild() {
        TreeBuilder tb = new TreeBuilder("mine");

        List<String> names = new LinkedList<String>();
        names.add("11");
        names.add("12");
        names.add("13");

        tb.addChildren(tb.getHead(), names);

        names.clear();
        for(HNode lvl : tb.getHead().children) {
            names.add("21" + lvl.id);
            names.add("22" + lvl.id);
        }


    }
}
