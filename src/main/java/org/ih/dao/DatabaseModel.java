package org.ih.dao;

import org.ih.dto.DataObject;

import java.io.Serializable;

/**
 * Interface for all Data Objects stored in the database
 *
 * @author Hector Plahar
 */
public interface DatabaseModel extends Serializable {

    DataObject toDataObject();
}
