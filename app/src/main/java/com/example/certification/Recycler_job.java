package com.example.certification;

public class Recycler_job {

    String NAME;
    String CATEGORY;
    String DESCRIPTION;
    String LINK;
    String CERTIFICATION_NAME;

    public Recycler_job(String NAME, String CATEGORY, String DESCRIPTION, String LINK, String CERTIFICATION_NAME) {
        this.NAME = NAME;
        this.CATEGORY = CATEGORY;
        this.DESCRIPTION = DESCRIPTION;
        this.LINK = LINK;
        this.CERTIFICATION_NAME = CERTIFICATION_NAME;
    }

    public String getName() {
        return NAME;
    }

    public String getCertification_name() {
        return CERTIFICATION_NAME;
    }

    public String getCategory() {
        return CATEGORY;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public String getLink() {
        return LINK;
    }
}
