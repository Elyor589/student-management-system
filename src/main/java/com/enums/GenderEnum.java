package com.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GenderEnum {
    MALE,
    FEMALE;

    @JsonCreator
    public static GenderEnum fromString(String gender) {
        return GenderEnum.valueOf(gender.toUpperCase());
    }
}

