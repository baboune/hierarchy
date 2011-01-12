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
 * User: Baboune
 * Date: 2010-dec-25
 * Time: 23:12:48
 */
package com.ericsson.edible.fasttrack.dao;

import com.ericsson.edible.fasttrack.object.HNode;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class comments ...
 */
@Stateless
public class NodeDao implements LocalNodeDao {

    /**
     * Logger instance
     */
    private static final Logger LOG = Logger.getLogger(NodeDao.class.getName());
    @PersistenceContext(unitName = "nodeservice")
    private EntityManager em = null;

    public HNode create(HNode ko) {
        em.persist(ko);
        // TODO: Probably remove -> right now guarantees an id...
        em.flush();
        return ko;
    }


    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public HNode get(final Long id) {
        if (id == null) {
            return null;
        }
        return em.find(HNode.class, id);
    }


    public void delete(final Long id) {
        HNode ko = get(id);
        if (ko != null) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Removing mapping for: " + id);
            }
            em.remove(ko);
        }
    }
}
