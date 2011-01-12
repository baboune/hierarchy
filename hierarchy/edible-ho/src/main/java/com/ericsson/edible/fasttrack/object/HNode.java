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
