package com.ericsson.edible.fasttrack.object;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Baboune
 * Date: 2010-dec-25
 * Time: 13:19:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "EDB_NODE")
public class HNode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id = null;

    public String name = null;
    public Long parentId = null;
    @Column(name  = "LVL")
    public Integer level = null;
}
