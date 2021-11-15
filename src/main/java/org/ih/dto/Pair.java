package org.ih.dto;

public class Pair implements DataObject {

    private String name;
    private int value;

    public Pair(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
