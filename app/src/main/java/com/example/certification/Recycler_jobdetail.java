package com.example.certification;

public class Recycler_jobdetail {

    String JOB_NAME;
    String JOB_CATEGORY;
    String DESCRIPTION;
    String LINK;
    String NUM;
    String NAME;

    public Recycler_jobdetail(String JOB_NAME, String JOB_CATEGORY, String DESCRIPTION, String LINK, String NUM, String NAME) {
        this.JOB_NAME = JOB_NAME;
        this.JOB_CATEGORY = JOB_CATEGORY;
        this.DESCRIPTION = DESCRIPTION;
        this.LINK = LINK;
        this.NUM = NUM;
        this.NAME = NAME;
    }

    public String getNAME() {
        return NAME;
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
