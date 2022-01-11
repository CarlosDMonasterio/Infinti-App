package org.ih.account;

import org.ih.dto.DataObject;

/**
 * @author Hector Plahar
 */
public enum AccountRole implements DataObject {
    REPORTS,            // can see the reports section
    PHI,                // can see personal health information of other users
    NURSE_MANAGER,      // nurse manager
    OPERATIONS_MANAGER, // operations manager
    ADMINISTRATOR       // global administrator with access to all parts of the application
}
