package com.example.biologic;

import java.util.ArrayList;

public class Note {
    public ArrayList<String> struct;
    public String name;

    public Note(String name,ArrayList<String> struct) {
        this.struct = struct;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
