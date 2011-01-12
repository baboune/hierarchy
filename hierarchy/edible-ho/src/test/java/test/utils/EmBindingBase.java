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
 * Date: 1/12/11
 * Time: 4:44 PM
 */
package test.utils;

import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 */
public class EmBindingBase {
    private static EntityManagerFactory emf = null;
    private EntityManager entityManager = null;


    @BeforeClass
    public static void setEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("test");
    }

    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            if (emf != null) {
                entityManager = emf.createEntityManager();
            } else {
                throw new RuntimeException("No persistence provider found.");
            }
        }
        return entityManager;
    }

    protected void beginTx() {
        getEntityManager().getTransaction().begin();
    }

    protected void commitTx() {
        if (!getEntityManager().getTransaction().getRollbackOnly()) {
            getEntityManager().getTransaction().commit();
        } else {
            rollbackTx();
        }
    }

    /**
     * Rolls back the transaction if a transaction is active.
     * The method is safe to call even if no transaction is active.
     */
    protected void rollbackTx() {
        if (getEntityManager().getTransaction().isActive()) {
            getEntityManager().getTransaction().rollback();
        }
    }
}

