package com.example.certification;

public class Recycler_certification {

    String NAME;
    String DESCRIPTION;
    String COMPANY;
    String JOB;
    String LINK;
    int NUM;
    String SUBJECT_WRITTEN;
    String SUBJECT_PRACTICAL;
    String RECEIPT_DATE;
    String WRITTEN_DATE;
    String PRACTICAL_DATE;
    String ANNOUNCEMENT_DATE;

    public Recycler_certification(String NAME, String DESCRIPTION, String COMPANY, String JOB, String LINK, int NUM, String SUBJECT_WRITTEN, String SUBJECT_PRACTICAL, String RECEIPT_DATE, String WRITTEN_DATE, String PRACTICAL_DATE, String ANNOUNCEMENT_DATE) {
        this.NAME = NAME;
        this.DESCRIPTION = DESCRIPTION;
        this.COMPANY = COMPANY;
        this.JOB = JOB;
        this.LINK = LINK;
        this.NUM = NUM;
        this.SUBJECT_WRITTEN = SUBJECT_WRITTEN;
        this.SUBJECT_PRACTICAL = SUBJECT_PRACTICAL;
        this.RECEIPT_DATE = RECEIPT_DATE;
        this.WRITTEN_DATE = WRITTEN_DATE;
        this.PRACTICAL_DATE = PRACTICAL_DATE;
        this.ANNOUNCEMENT_DATE = ANNOUNCEMENT_DATE;
    }

    public String getNAME() {
        return NAME;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getCOMPANY() {
        return COMPANY;
    }

    public String getJOB() {
        return JOB;
    }

    public String getLINK() {
        return LINK;
    }

    public int getNUM() {
        return NUM;
    }

    public String getSUBJECT_WRITTEN() {
        return SUBJECT_WRITTEN;
    }

    public String getSUBJECT_PRACTICAL() {
        return SUBJECT_PRACTICAL;
    }

    public String getRECEIPT_DATE() {
        return RECEIPT_DATE;
    }

    public String getWRITTEN_DATE() {
        return WRITTEN_DATE;
    }

    public String getPRACTICAL_DATE() {
        return PRACTICAL_DATE;
    }

    public String getANNOUNCEMENT_DATE() {
        return ANNOUNCEMENT_DATE;
    }
}
