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

import com.ericsson.edible.fasttrack.H;
import com.ericsson.edible.fasttrack.TreeBuilder;
import com.ericsson.edible.fasttrack.exception.InvalidTreeException;
import com.ericsson.edible.fasttrack.object.HNode;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @Override
    public HNode persist(TreeBuilder tb) throws InvalidTreeException {
        HNode head = tb.getHead();
        if (head == null) {
            return null;
        }
        if (head.parent == null) {
            head = create(head);
            createLevel(head);
            return head;
        } else if (head.parent != null && head.parent.id != null) {
            HNode parent = get(head.parent.id);
            return update(parent, head.children);
        }
        throw new InvalidTreeException("Invalid head");
    }

    public HNode update(HNode parent, List<HNode> children) {
        return null;
    }

    public void moveSubTree(Long subTreeHeadId, Long newParentId) {
        HNode what = em.find(HNode.class, subTreeHeadId);
        HNode where = em.find(HNode.class, newParentId);

        what.parent = where;
        what.lineage = where.lineage + '/' + where.id;
        what.level = where.level + 1;

        // edit the rest of the children
        rebuildLineage(what);
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
    public HNode getFirstChild() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HNode getLastChild() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HNode getPreviousSibling() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HNode getNextSibling() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void create(HNode id, Object o) {
        // dummy method used as a reminder of higher level function.
    }

    private void createLevel(HNode head) throws InvalidTreeException {
        // get the id of the new head
        if (head.id == null) {
            em.flush();
        }
        // Build a new tree
        for (HNode h : head.children) {
            h.parent = head;
            if ("/".equals(head.lineage)) {
                h.lineage = "/" + head.id;
            } else {
                h.lineage = head.lineage + '/' + head.id;
            }
            h.level = head.level + 1;
            create(h);
            if (h.children != null && h.children.size() > 0) {
                createLevel(h);
            }
        }
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
