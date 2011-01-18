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
 * Time: 13:19:04
 */
package com.ericsson.edible.fasttrack.object;

import org.apache.openjpa.persistence.jdbc.Index;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = "EDB_NODE")
public class HNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id = null;
    @Index
    public String name = null;
    @Column(name = "LVL")
    public Integer level = null;
    @Index
    public String lineage = "";

    @ManyToOne()
    @JoinColumn(name = "PARENT_ID")
    @Index
    public HNode parent;

    @Transient
    public List<HNode> children = new LinkedList<HNode>();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(200);
        builder.append("HNode{");

        String s = "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level + ", parent=";

        builder.append(s);

        if (parent != null) {
            builder.append(parent.id);
        } else {
            builder.append("null");
        }
        s = ", children=" + children + '}';
        builder.append(s);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HNode hNode = (HNode) o;

        if (level != null ? !level.equals(hNode.level) : hNode.level != null) return false;
        if (lineage != null ? !lineage.equals(hNode.lineage) : hNode.lineage != null) return false;
        if (name != null ? !name.equals(hNode.name) : hNode.name != null) return false;

        // compare parent id
        if (parent != null) {
            if (parent.id != null ? !parent.id.equals(hNode.parent.id) : hNode.parent.id != null) return false;
        } else {
            // otherwise return true if both are null
            return hNode.parent == null;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (lineage != null ? lineage.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.id.hashCode() : 0);
        return result;
    }
}
