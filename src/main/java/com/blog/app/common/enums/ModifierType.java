package com.blog.app.common.enums;

public enum ModifierType {
    SYSTEM("SYSTEM"),
    USER("USER"),
    ADMIN("ADMIN");

    ModifierType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }
}
