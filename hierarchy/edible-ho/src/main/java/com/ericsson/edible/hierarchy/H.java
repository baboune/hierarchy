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
package com.ericsson.edible.hierarchy;

import com.ericsson.edible.hierarchy.exception.InvalidTreeException;
import com.ericsson.edible.hierarchy.object.HNode;

import java.util.List;

/**
 *
 */
public interface H {

    /**
     * Creates a tree head.
     *
     * @param name The name of the head node.
     *
     * @return The persisted head.
     * @throws InvalidTreeException In case such a head already exists.
     */
    public HNode createHead(String name) throws InvalidTreeException;


    /**
     * Convenience method to locate a node based on a name.
     * Note that the node name is not unique (should it be?)
     * so this can return multiple nodes.
     *
     * @param name A {@link String}.
     * @return A list of {@link HNode} nodes that share this name.
     */
    List<HNode> findNodeByName(String name);


    /**
     * The absolute parent of the hierarchy. All nodes have a parent.
     *
     * @return The {@link HNode}.
     */
    HNode getParentNode();

    /**
     * The parent of this node as specified by the id. All nodes.
     * However, if a node has just been created and not yet added to the
     * tree, or if it has been removed from the tree, this is
     * <code>null</code>.
     *
     * @return The {@link HNode}.
     */
    HNode getParentNode(Long id);

    /**
     * A <code>NodeList</code> that contains all children of this node. If
     * there are no children, this is a <code>NodeList</code> containing no
     * nodes.
     */
    List<HNode> getChildren(Long id);

    List<HNode> getChildren(HNode node);

    /**
     * The first child of this node. If there is no such node, this returns
     * <code>null</code>.
     */
    HNode getFirstChild();

    /**
     * The last child of this node. If there is no such node, this returns
     * <code>null</code>.
     */
    HNode getLastChild();

    /**
     * The node immediately preceding this node. If there is no such node,
     * this returns <code>null</code>.
     */
    HNode getPreviousSibling();

    /**
     * The node immediately following this node. If there is no such node,
     * this returns <code>null</code>.
     */
    HNode getNextSibling();

    /**
     * Higher level API from provisioning probably.
     * <p/>
     * Used for createUser(parentId, User) for example.
     * Used for createDepartment(parentId, Department) for example.
     *
     * @param id
     * @param o
     */
    public void relate(HNode id, Object o);

}
