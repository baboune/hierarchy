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
package com.ericsson.edible.fasttrack.dao;

import com.ericsson.edible.fasttrack.object.HNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;

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
    public void test1() {
        HNode n = new HNode();
        n.name = "head";
        n.level = 0;

        beginTx();
        HNode no = dao.create(n);
        getEntityManager().flush();
        commitTx();
        assertEquals("head", no.name);

        assertNull(no.parentId);

        getEntityManager().clear();

        Long id = no.id;

        HNode ln = new HNode();
        ln.name = "lchild";
        ln.parentId = id;
        ln.level = no.level +1;

        HNode rn = new HNode();
        rn.name = "rchild";
        rn.parentId = id;
        rn.level = no.level +1;

        beginTx();
        dao.create(ln);
        dao.create(rn);
        getEntityManager().flush();
        commitTx();

        getEntityManager().clear();


    }


}

