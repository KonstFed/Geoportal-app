package com.example.biologic;

import java.util.ArrayList;

public class Table {
    String name;
    ArrayList<String> struct;

    public Table(String name, ArrayList<String> struct) {
        this.name = name;
        this.struct = struct;
    }

    @Override
    public String toString() {
        return name;
    }
}
