package org.ih.dto;

import java.util.ArrayList;
import java.util.List;

public class HygieneGraphData implements DataObject {

    private String name;
    private final List<Pair> series;

    public HygieneGraphData() {
        this.series = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Pair> getSeries() {
        return series;
    }
}
