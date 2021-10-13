package org.ih.common;

import org.ih.dto.DataObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper around requested list of <code>DataObject</code>s to include
 * total available. The requested list is typically less than the amount available
 *
 * @author Hector Plahar
 */
public class Results<T extends DataObject> implements DataObject {

    private List<T> requested;
    private long available;

    public Results() {
        this.requested = new LinkedList<>();
    }

    public List<T> getRequested() {
        return requested;
    }

    public void setRequested(List<T> requested) {
        if (requested == null)
            throw new NullPointerException();
        this.requested = new LinkedList<>(requested);
    }

    public boolean add(T datum) {
        return this.requested.add(datum);
    }

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long available) {
        this.available = available;
    }
}
