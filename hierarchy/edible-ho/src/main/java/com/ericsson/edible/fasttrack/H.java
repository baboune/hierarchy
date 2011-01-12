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
package com.ericsson.edible.fasttrack;

import com.ericsson.edible.fasttrack.object.HNode;

import java.util.List;

/**
 *
 */
public interface H {
    /**
     * The parent of this node. All nodes, except <code>Attr</code>,
     * <code>Document</code>, <code>DocumentFragment</code>,
     * <code>Entity</code>, and <code>Notation</code> may have a parent.
     * However, if a node has just been created and not yet added to the
     * tree, or if it has been removed from the tree, this is
     * <code>null</code>.
     */
    public HNode getParentNode();

    /**
     * A <code>NodeList</code> that contains all children of this node. If
     * there are no children, this is a <code>NodeList</code> containing no
     * nodes.
     */
    public List<HNode> getChildNodes();

    /**
     * The first child of this node. If there is no such node, this returns
     * <code>null</code>.
     */
    public HNode getFirstChild();

    /**
     * The last child of this node. If there is no such node, this returns
     * <code>null</code>.
     */
    public HNode getLastChild();

    /**
     * The node immediately preceding this node. If there is no such node,
     * this returns <code>null</code>.
     */
    public HNode getPreviousSibling();

    /**
     * The node immediately following this node. If there is no such node,
     * this returns <code>null</code>.
     */
    public HNode getNextSibling();
}
