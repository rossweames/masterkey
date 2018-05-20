package com.eames.masterkey.model;

/**
 * This interface defines the operations mandatory for all classes that support children.
 */
public interface HasChildren<T> {

    /**
     * Gets the children.
     *
     * @return the children
     */
    T[] getChildren();

    /**
     * Sets the children.
     *
     * @param children the new children
     */
    void setChildren(T[] children);
}
