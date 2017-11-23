package com.ssslinppp.model;

import lombok.Data;

@Data
public class Book {
    private String name;
    private String auther;
    private BookCategory category;

    public enum BookCategory {
        java,
        mysql;
    }
}
