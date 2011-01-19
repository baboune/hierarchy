/**
 * Copyright (c) Nicolas Seyvet, 2010.
 *
 * All Rights Reserved. Reproduction in whole or in part is prohibited
 * without the written consent of the copyright owner.
 *
 * Nicolas Seyvet MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. ERICSSON SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * User: Baboune
 * Date: 19/01/11
 * Time: 22:03
 */
package com.ericsson.edible.hierarchy.utils;

/**
 *
 */
public final class HierarchyHelper {
    public static final String L_SEP = "/";

    /**
     * Prevent class instantiation.
     */
    protected HierarchyHelper() {
    }

    public static String makeLineage(final String lineage, final Long parentNodeId) {

        if (lineage == null || L_SEP.equals(lineage)) {
            StringBuilder builder = new StringBuilder(10);
            return builder.append(L_SEP).append(parentNodeId).toString();
        }
        StringBuilder builder = new StringBuilder(lineage.length() + 10);
        return builder.append(lineage).append(L_SEP).append(parentNodeId).toString();

    }
}
