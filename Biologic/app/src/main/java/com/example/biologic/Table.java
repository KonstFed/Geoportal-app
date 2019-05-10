package com.example.biologic;

import org.json.JSONArray;

import java.util.ArrayList;

public class Table {
    String nameTable;
    JSONArray struct;
    ArrayList<Note> notes;
    public Table(String name, JSONArray struct,ArrayList<Note> notes) {
        this.nameTable = name;
        this.struct = struct;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return nameTable;
    }
}
