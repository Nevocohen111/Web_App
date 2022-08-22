package com.example.demo.model;

import lombok.Data;

@Data
public class Holiday {
    private final String day;
    private final String reason;
    private final Type type;
    private final String url;

    public enum Type {
        FEDERAL,
        FESTIVAL,
    }

}
