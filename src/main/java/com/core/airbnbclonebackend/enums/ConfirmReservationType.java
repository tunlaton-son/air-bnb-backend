package com.core.airbnbclonebackend.enums;

public enum ConfirmReservationType {

    ASK_TO_BOOK("ASK_TO_BOOK"),
    AUTO_TO_BOOK("AUTO_TO_BOOK"),
    ;

    private final String value;

    ConfirmReservationType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
