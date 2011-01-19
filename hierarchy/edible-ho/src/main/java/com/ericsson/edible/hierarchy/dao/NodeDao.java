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
package com.ericsson.edible.hierarchy.dao;

import com.ericsson.edible.hierarchy.H;
import com.ericsson.edible.hierarchy.exception.InvalidTreeException;
import com.ericsson.edible.hierarchy.object.HNode;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class comments ...
 */
@Stateless
public class NodeDao implements LocalNodeDao, H {

    /**
     * Logger instance
     */
    private static final Logger LOG = Logger.getLogger(NodeDao.class.getName());
    @PersistenceContext(unitName = "nodeservice")
    private EntityManager em = null;


    /**
     * {@inheritDoc}
     */
    public HNode create(HNode ko) throws InvalidTreeException {
        if (ko.parent == null) {
            ko.lineage = "/";
            // check if there is already a top parent node
            Number nb = (Number) em.createQuery("SELECT COUNT(h) FROM HNode h WHERE h.parent IS NULL").getSingleResult();
            if (nb.intValue() > 0) {
                throw new InvalidTreeException("dual heads are not supported");
            }
        }
        em.persist(ko);
        return ko;
    }


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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
            // remove all children
            // SELECT * FROM edb_node where parent_id = 1 OR lineage LIKE ('/1/%');

            if (ko.lineage == null || ko.lineage.length() <= 1) {
                deleteAll();
            } else {
                String lineage = ko.lineage + '/' + id + "/%";


                Query q = em.createQuery("DELETE FROM HNode h  WHERE h.parent.id = :deadNode " +
                        "OR h.lineage LIKE :lineage");
                q.setParameter("deadNode", id);
                System.out.println("lineage to del:" + lineage);
                q.setParameter("lineage", lineage);
                int nb = q.executeUpdate();
                System.out.println("Removed " + nb + " nodes");
            }
        }
    }

    public HNode update(HNode parent, List<HNode> children) {
        return null;
    }

    /**
     * H implementation
     */

    /**
     * {@inheritDoc}
     */
    public HNode createHead(String name) throws InvalidTreeException {
        HNode head = new HNode();
        head.name = name;
        return create(head);
    }


    public void moveSubTree(Long subTreeHeadId, Long newParentId) {
        HNode head = em.find(HNode.class, subTreeHeadId);
        HNode where = em.find(HNode.class, newParentId);

        head.parent = where;
        head.lineage = where.lineage + '/' + where.id;
        head.level = where.level + 1;

        // edit the rest of the children
        rebuildLineage(head);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public HNode getParentNode() {
        return (HNode) em.createQuery("SELECT n FROM HNode n WHERE n.parent IS NULL").getSingleResult();
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public HNode getParentNode(final Long id) {
        if (id == null) {
            return getParentNode();
        } else {
            return (HNode) em.createQuery("SELECT n FROM HNode n WHERE n.parent = :id")
                    .setParameter("id", id).getSingleResult();
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @SuppressWarnings("unchecked")
    public List<HNode> findNodeByName(String name) {
        Query q = em.createQuery("SELECT h FROM HNode h WHERE h.name = :name");
        q.setParameter("name", name);
        return q.getResultList();

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @SuppressWarnings("unchecked")
    public List<HNode> getChildren(final Long id) {
        Query q = em.createQuery("SELECT n FROM HNode n WHERE n.parent.id = :id");
        q.setParameter("id", id);
        return q.getResultList();
    }

    @Override
    public List<HNode> getChildren(HNode node) {
        if (node == null) {
            return new LinkedList<HNode>();
        }
        return getChildren(node.id);
    }

    @Override
    public HNode getFirstChild() {
        return null;
    }

    @Override
    public HNode getLastChild() {
        return null;
    }

    @Override
    public HNode getPreviousSibling() {
        return null;
    }

    @Override
    public HNode getNextSibling() {
        return null;
    }

    @Override
    public void relate(HNode id, Object o) {
        // dummy method used as a reminder of higher level function.
    }

    private void rebuildLineage(HNode head) {
        // get the id of the new head
        if (head.id == null) {
            return;
        }

        // Build a new tree
        List<HNode> grandChildren = null;
        for (HNode h : getChildren(head.id)) {
            h.lineage = head.lineage + '/' + head.id;
            h.level = head.level + 1;
            em.merge(h);
            grandChildren = getChildren(head.id);
            if (grandChildren != null && grandChildren.size() > 0) {
                rebuildLineage(h);
            }
        }
    }

    private int deleteAll() {
        Query q = em.createQuery("SELECT COUNT(h) FROM HNode h");
        Number nb = (Number) q.getSingleResult();

        for (int i = 0; i < nb.intValue(); i += 30000) {
            q = em.createQuery("DELETE FROM HNode h");
            q.executeUpdate();
        }
        return nb.intValue();
    }


}
