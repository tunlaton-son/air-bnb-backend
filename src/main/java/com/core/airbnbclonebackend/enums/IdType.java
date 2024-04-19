package com.core.airbnbclonebackend.enums;

public enum IdType {
    DRIVER_LICENSE("DRIVER_LICENSE"),
    PASSPORT("PASSPORT"),
    IDENTITY_CARD("IDENTITY_CARD"),
    ;

    private final String value;

    IdType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
