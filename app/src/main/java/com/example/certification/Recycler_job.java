package com.example.certification;

public class Recycler_job {

    String JOB_NAME;
    String JOB_CATEGORY;
    String NUM;

    public Recycler_job(String JOB_NAME, String JOB_CATEGORY, String NUM) {
        this.JOB_NAME = JOB_NAME;
        this.JOB_CATEGORY = JOB_CATEGORY;
        this.NUM = NUM;
    }

    public String getNUM() {
        return NUM;
    }

    public String getJOB_CATEGORY() {
        return JOB_CATEGORY;
    }

    public String getJOB_NAME() {
        return JOB_NAME;
    }
}
