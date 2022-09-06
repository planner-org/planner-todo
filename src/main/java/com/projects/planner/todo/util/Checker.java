package com.projects.planner.todo.util;

public final class Checker {
    private Checker() {
        throw new IllegalStateException("Util class can't have an instance");
    }

    public static void idIsNullOrZero(Long id) {
        if (id == null || id == 0) {
            throw new IllegalArgumentException("Missing param: Id must be null");
        }
    }

    public static void idNotNullAndNotZero(Long id) {
        if (id != null && id != 0) {
            throw new IllegalArgumentException("Missing param: Id must not be null");
        }
    }

    public static void titleNotNullAndNotEmpty(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing param: TITLE must not be null or empty");
        }
    }

}
