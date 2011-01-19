/**
 * Copyright (c) Nicolas Seyvet, 2010.
 *
 * All Rights Reserved. Reproduction in whole or in part is prohibited
 * without the written consent of the copyright owner.
 *
 * Nicolas Seyvet MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. ERICSSON SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * User: Baboune
 * Date: 19/01/11
 * Time: 21:41
 */
package com.ericsson.edible.hierarchy.dao;

import com.ericsson.edible.hierarchy.exception.InvalidTreeException;
import com.ericsson.edible.hierarchy.object.HNode;
import org.apache.openjpa.lib.conf.*;

import javax.persistence.EntityManager;

/**
 * Util class for trees.
 */
public class TreeBuilder {
    NodeDao dao = null;
    String tname = "";
    EntityManager em = null;

    public TreeBuilder(EntityManager em, NodeDao dao, String tname) {
        this.dao = dao;
        this.tname = tname;
        this.em = em;
    }

    /**
     * Builds a balanced tree with (i) level and (j) element per depth.
     * <p/>
     * Ex: (2,2)
     *          name
     *      11         12
     *   111 112    121 122
     *
     * @param maxDepth      Nb of levels.
     * @param nbElements Nb of elements per maxDepth
     * @return The head of the tree
     */
    public HNode buildBalancedTree(int maxDepth, int nbElements) throws Exception {
        HNode head = dao.createHead(tname);
        em.flush();
        if (maxDepth > 1) {
            createLevel(head, nbElements, maxDepth, 1);
        }
        return head;
    }

    public void createLevel(HNode head, int nb, int maxDepth, int currDepth) throws InvalidTreeException {
        // Build a new level
        em.flush();
        for (int i = 0; i < nb; i++) {
            String name = head.name + (i + 1);
            if(tname.equals(head.name)){
                name = String.valueOf(currDepth) + String.valueOf(i + 1);
            }
            HNode node = new HNode(name, head);
            node = dao.create(node);
            if (currDepth < maxDepth ) {
                createLevel(node, nb, maxDepth, currDepth + 1);
            }
        }
    }
}
