package com.example.reports.entity;

public enum DocumentType {
    TYPE1("T1"), TYPE2("T2"), TYPE3("T3");

    private final String code;

    DocumentType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
