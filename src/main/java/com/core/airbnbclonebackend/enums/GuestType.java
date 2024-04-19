package com.core.airbnbclonebackend.enums;

public enum GuestType {

    ANY_GUEST("ANY_GUEST"),
    EXPERIENCED_GUEST("EXPERIENCED_GUEST");

    private final String value;

    GuestType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
