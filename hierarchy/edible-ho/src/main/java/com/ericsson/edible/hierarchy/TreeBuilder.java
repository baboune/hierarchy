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
 * Time: 2:18 PM
 */
package com.ericsson.edible.hierarchy;

import com.ericsson.edible.hierarchy.object.HNode;

import java.util.List;

/**
 *
 */
public class TreeBuilder {
    HNode head = null;

    public TreeBuilder(String name){
        head = new HNode();
        head.level = 0;
        head.name = name;
    }

    public TreeBuilder(HNode head){
        this.head = head;
    }

    public HNode getHead() {
        return head;
    }

    public TreeBuilder addChildren(HNode head, List<String> children) {
        for(String n: children) {
            HNode child = new HNode();
            child.name = n;
            child.level = head.level + 1;
            head.children.add(child);
            child.parent = head;
        }
        return this;
    }

    @Override
    public String toString() {
        return "TreeBuilder{" +
                "head=" + head +
                '}';
    }
}
