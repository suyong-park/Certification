package com.example.certification;

public class Recycler_jobdetail {

    String JOB_NAME;
    String JOB_CATEGORY;
    String DESCRIPTION;
    String LINK;
    String NUM;

    public Recycler_jobdetail(String JOB_NAME, String JOB_CATEGORY, String DESCRIPTION, String LINK, String NUM) {
        this.JOB_NAME = JOB_NAME;
        this.JOB_CATEGORY = JOB_CATEGORY;
        this.DESCRIPTION = DESCRIPTION;
        this.LINK = LINK;
        this.NUM = NUM;
    }

    public String getJOB_NAME() {
        return JOB_NAME;
    }

    public String getJOB_CATEGORY() {
        return JOB_CATEGORY;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getLINK() {
        return LINK;
    }

    public String getNUM() {
        return NUM;
    }
}
