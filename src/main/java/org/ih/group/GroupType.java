package org.ih.group;

/**
 * Group type attribute
 *
 * @author Hector Plahar
 */
public enum GroupType {
    /**
     * Created by an administrator and confers read permissions to all projects members create
     */
    PUBLIC,

    /**
     * Group created by researchers to aggregate selected users
     */
    PRIVATE
}
