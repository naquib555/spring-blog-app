package com.blog.app.common.enums;

public enum Flag {
    ACTIVE("1"),
    INACTIVE("0"),

    YES("Y"),
    NO("N");

    private String flag;

    Flag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }
}
