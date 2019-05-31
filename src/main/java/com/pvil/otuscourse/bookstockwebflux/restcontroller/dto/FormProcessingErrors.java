package com.pvil.otuscourse.bookstockwebflux.restcontroller.dto;

import org.springframework.validation.Errors;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormProcessingErrors {
    private Map<String, String> fields;

    private List<String> global;

    public FormProcessingErrors() {
        this.fields = new HashMap<>();
        this.global = new ArrayList<>();
    }

    public FormProcessingErrors(Errors fields) {
        this();
        fields.getFieldErrors().forEach(e -> this.fields.put(e.getField(), e.getDefaultMessage()));
        fields.getGlobalErrors().forEach(e -> this.global.add(e.getDefaultMessage()));
    }

    public FormProcessingErrors(WebExchangeBindException fields) {
        this();
        fields.getFieldErrors().forEach(e -> this.fields.put(e.getField(), e.getDefaultMessage()));
        fields.getGlobalErrors().forEach(e -> this.global.add(e.getDefaultMessage()));
    }

    public FormProcessingErrors(String globalError) {
        this();
        this.global.add(globalError);
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public List<String> getGlobal() {
        return global;
    }

    public void setGlobal(List<String> global) {
        this.global = global;
    }
}
