package com.indra.checkmynationality;

import java.util.ArrayList;

public class Root{
    private String name;
    private ArrayList<Country> country;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Country> getCountry() {
        return country;
    }
    public void setCountry(ArrayList<Country> country) {
        this.country = country;
    }
}