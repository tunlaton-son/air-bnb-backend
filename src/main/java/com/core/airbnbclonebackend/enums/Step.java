package com.core.airbnbclonebackend.enums;

import java.util.Arrays;
import java.util.Objects;

public enum Step {

    STEP1(0),
    CATEGORY(1),
    TYPE(2),
    LOCATION(3),
    LOCATION_SUMMARY(4),
    INFO(5),
    STEP2(6),
    OFFERS(7),
    IMAGES(8),
    TITLES(9),
    DESCRIBE(10),
    STEP3(11),
    RESERVATIONS(12),
    RESERVATION_TYPE(13),
    PRICE(14),
    DISCOUNTS(15),
    SECURITY(16),
    REVIEW(17),
    CELEBRATION(18),
    VERIFY_LISTING(19),;

    private final Integer value;

    Step(Integer value) {
        this.value = value;
    }

    public Step getStep(Integer step){
        return Arrays.stream(Step.values()).filter(e-> Objects.equals(e.value, step)).findFirst().orElse(null);
    }

    public Step getNextStep() {
        return Arrays.stream(Step.values()).filter(e->e.value == this.value + 1).findFirst().orElse(this);
    }
}
