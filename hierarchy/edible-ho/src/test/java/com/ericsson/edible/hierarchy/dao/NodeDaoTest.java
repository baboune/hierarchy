/**
 *
 * Copyright (c) Ericsson AB, 2009.
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
 * Date: 2010-mar-09
 */
package com.ericsson.edible.hierarchy.dao;

import com.ericsson.edible.hierarchy.object.HNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import test.utils.EmBindingBase;

import javax.persistence.Query;


/**
 * Key Dao test cases
 */
public class NodeDaoTest extends EmBindingBase {

    NodeDao dao = null;

    @Before
    public void setup() throws Exception {
        dao = new NodeDao();

        beginTx();
        Query q = getEntityManager().createNativeQuery("DELETE FROM EDB_NODE");
        q.executeUpdate();
        commitTx();

        Field f = dao.getClass().getDeclaredField("em");
        f.setAccessible(true);
        f.set(dao, getEntityManager());
    }

    @Test
    public void testThreeNodeTree() throws Exception {
        HNode n = new HNode();
        n.name = "head";
        n.level = 0;

        beginTx();
        HNode no = dao.create(n);
        getEntityManager().flush();
        commitTx();
        assertEquals("head", no.name);
        assertNull(no.parent);

        // clean up em
        getEntityManager().clear();

        HNode ln = new HNode();
        ln.name = "lchild";
        ln.parent = no;
        ln.level = no.level + 1;

        HNode rn = new HNode();
        rn.name = "rchild";
        rn.parent = no;
        rn.level = no.level + 1;

        beginTx();
        dao.create(ln);
        dao.create(rn);
        commitTx();

        // clean up em
        getEntityManager().clear();

        // verify tree
        HNode head = dao.getParentNode();
        assertNotNull(head);

        List<HNode> children = dao.getChildren(head);
        assertNotNull(children);
        assertEquals(2, children.size());

        boolean[] found = new boolean[2];
        for (HNode h : children) {
            if ("lchild".equals(h.name)) {
                found[0] = true;
            }
            if ("rchild".equals(h.name)) {
                found[1] = true;
            }
        }
        assertTrue(found[0]);
        assertTrue(found[1]);

    }

    @Test
    public void testTB() throws Exception {
        TreeBuilder tb = new TreeBuilder(getEntityManager(), dao, "testBT");

        beginTx();
        tb.buildBalancedTree(2, 2);
        commitTx();

        // clean em
        getEntityManager().clear();

        HNode head = dao.getParentNode();
        assertNotNull(head);

        List<HNode> children = dao.getChildren(head.id);
        assertNotNull(children);
        assertEquals(2, children.size());

        List<HNode> grandChildren = null;
        for (HNode child : children) {
            grandChildren = dao.getChildren(child.id);
            assertEquals(2, grandChildren.size());
        }

        // clean em
        getEntityManager().clear();

        List<HNode> toDeletes = dao.findNodeByName("12");
        assertNotNull(toDeletes);
        assertEquals(1, toDeletes.size());

        beginTx();
        dao.delete(toDeletes.get(0).id);
        commitTx();

        // clean em
        getEntityManager().clear();

        // verify it is not there
        assertNull(dao.get(toDeletes.get(0).id));

        // check tree
        children = dao.getChildren(head.id);
        assertNotNull(children);
        assertEquals(1, children.size());
    }

    @Test
    public void testMoveSubtree() throws Exception {
        TreeBuilder tb = new TreeBuilder(getEntityManager(), dao, "testMoveSubtree");

        beginTx();
        tb.buildBalancedTree(3, 3);
        commitTx();

        // clean em
        getEntityManager().clear();

        List<HNode> nodes = dao.findNodeByName("12");
        // for the test there can be only one
        assertEquals(1, nodes.size());

        List<HNode> nodesToMoveTo = dao.findNodeByName("11");
        // for the test there can be only one
        assertEquals(1, nodesToMoveTo.size());

        // move 12 to 11
        long idToMove = nodes.get(0).id;
        long newParentId = nodesToMoveTo.get(0).id;

        beginTx();
        dao.moveSubTree(idToMove, newParentId);
        commitTx();

        // clean em
        getEntityManager().clear();

        nodes = dao.getChildren(newParentId);
        boolean found = false;
        for (HNode child : nodes) {
            if ("12".equals(child.name)) {
                found = true;
            }

        }
        assertTrue(found);
    }


}

