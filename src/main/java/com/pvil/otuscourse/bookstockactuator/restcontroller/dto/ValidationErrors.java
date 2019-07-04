package com.pvil.otuscourse.bookstockactuator.restcontroller.dto;

import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationErrors {
    private Map<String, String> fields;

    private List<String> global;

    public ValidationErrors() {
        this.fields = new HashMap<>();
        this.global = new ArrayList<>();
    }

    public ValidationErrors(Errors fields) {
        this();
        fields.getFieldErrors().forEach(e -> this.fields.put(e.getField(), e.getDefaultMessage()));
        fields.getGlobalErrors().forEach(e -> this.global.add(e.getDefaultMessage()));
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
