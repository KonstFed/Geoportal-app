package com.example.biologic;

import java.util.ArrayList;

public class Note{
    public ArrayList<String> data;
    public String name;

    public Note(String name,ArrayList<String> data) {
        this.data = data;
        this.name = name;
    }
    public String getData()
    {
        String res = "";
        for (int i = 0; i < data.size()-1; i++) {
            res = res + data.get(i) +"/:";
        }
        res = res + data.get(data.size()-1);
        return res;
    }
    @Override
    public String toString() {
        return name;
    }
}
