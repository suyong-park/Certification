package com.example.certification;

public class Recycler_certifi {

    String NAME;
    String CATEGORY;

    public Recycler_certifi(String NAME, String CATEGORY) {
        this.NAME = NAME;
        this.CATEGORY = CATEGORY;
    }

    public String getCategory() {
        return CATEGORY;
    }

    public String getTitle() {
        return NAME;
    }
}
