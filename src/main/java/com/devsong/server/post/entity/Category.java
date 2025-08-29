package com.devsong.server.post.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    PROJECT("project"),
    STUDY("study"),
    EXTRA("extra"),
    FREE("free"),
    INFO("info");
    // 프로젝트, 스터디, 대외활동, 정보, 자유

    private final String value;

    Category(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Category from(String value) {
        for (Category category : Category.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        return null;
    }
}