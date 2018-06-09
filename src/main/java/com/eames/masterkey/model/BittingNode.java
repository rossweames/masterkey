package com.eames.masterkey.model;

/**
 * This interface defines all operations mandatory for all classes that are nodes in a bitting list.
 *
 * For example: {@link BittingGroup} and {@link KeyBitting}.
 */
public interface BittingNode {

    /**
     * Tests whether the bitting node has child groups.
     *
     * @return {@code True} if the node has groups, {@code false} if not
     */
    boolean hasGroups();
}
