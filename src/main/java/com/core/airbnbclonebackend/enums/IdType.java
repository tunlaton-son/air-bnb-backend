package com.core.airbnbclonebackend.enums;

public enum IdType {
    DRIVER_LICENSE("DRIVER_LICENSE"),
    PASSPORT("PASSPORT"),
    ID_CARD("ID_CARD"),
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
