package com.example.certification;

public class Recycler_category {

    String NAME;
    String CATEGORY;
    String NUM;

    public Recycler_category(String NAME, String CATEGORY, String NUM) {
        this.NAME = NAME;
        this.CATEGORY = CATEGORY;
        this.NUM = NUM;
    }

    public String getCategory() {
        return CATEGORY;
    }

    public String getNum() {
        return NUM;
    }

    public String getTitle() {
        return NAME;
    }
}
