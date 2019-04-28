package com.example.biologic;

import java.util.ArrayList;

public class Table {
    String nameTable;
    ArrayList<String> struct;
    ArrayList<Note> notes;
    public Table(String name, ArrayList<String> struct,ArrayList<Note> notes) {
        this.nameTable = name;
        this.struct = struct;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return nameTable;
    }
}
