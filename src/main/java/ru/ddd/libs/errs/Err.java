package ru.ddd.libs.errs;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

public final class Err {

    private static final UUID EMPTY_UUID = new UUID(0L, 0L);

    private Err() {
    }

    @SafeVarargs
    public static Error combine(Error... errors) {
        for (Error e : errors) {
            if (e != null)
                return e; // возвращаем первую ошибку
        }
        return null;
    }

    // ======================
    // String
    // ======================

    public static Error againstNullOrEmpty(String value, String paramName) {
        if (value == null || value.isBlank())
            return GeneralErrors.valueIsRequired(paramName);
        return null;
    }

    // ======================
    // UUID
    // ======================

    public static Error againstNullOrEmpty(UUID uuid, String paramName) {
        if (uuid == null || uuid.equals(EMPTY_UUID))
            return GeneralErrors.valueIsRequired(paramName);
        return null;
    }

    // ======================
    // Collection
    // ======================

    public static Error againstNullOrEmpty(Collection<?> collection, String paramName) {
        if (collection == null || collection.isEmpty())
            return GeneralErrors.valueIsRequired(paramName);
        return null;
    }

    // ======================
    // Numbers >= 0
    // ======================

    public static Error againstNegative(int value, String paramName) {
        if (value < 0)
            return GeneralErrors.valueIsInvalid(paramName, "must be >= 0");
        return null;
    }

    public static Error againstNegative(long value, String paramName) {
        if (value < 0L)
            return GeneralErrors.valueIsInvalid(paramName, "must be >= 0");
        return null;
    }

    public static Error againstNegative(float value, String paramName) {
        if (value < 0f)
            return GeneralErrors.valueIsInvalid(paramName, "must be >= 0");
        return null;
    }

    public static Error againstNegative(double value, String paramName) {
        if (value < 0d)
            return GeneralErrors.valueIsInvalid(paramName, "must be >= 0");
        return null;
    }

    public static Error againstNegative(BigDecimal value, String paramName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0)
            return GeneralErrors.valueIsInvalid(paramName, "must be >= 0");
        return null;
    }

    // ======================
    // Numbers > 0
    // ======================

    public static Error againstZeroOrNegative(int value, String paramName) {
        if (value <= 0)
            return GeneralErrors.valueIsInvalid(paramName, "must be > 0");
        return null;
    }

    public static Error againstZeroOrNegative(long value, String paramName) {
        if (value <= 0L)
            return GeneralErrors.valueIsInvalid(paramName, "must be > 0");
        return null;
    }

    public static Error againstZeroOrNegative(float value, String paramName) {
        if (value <= 0f)
            return GeneralErrors.valueIsInvalid(paramName, "must be > 0");
        return null;
    }

    public static Error againstZeroOrNegative(double value, String paramName) {
        if (value <= 0d)
            return GeneralErrors.valueIsInvalid(paramName, "must be > 0");
        return null;
    }

    public static Error againstZeroOrNegative(BigDecimal value, String paramName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0)
            return GeneralErrors.valueIsInvalid(paramName, "must be > 0");
        return null;
    }

    // ======================
    // Range
    // ======================

    public static Error againstOutOfRange(int value, int min, int max, String paramName) {
        if (value < min || value > max)
            return GeneralErrors.valueIsOutOfRange(paramName, value, min, max);
        return null;
    }

    public static Error againstOutOfRange(long value, long min, long max, String paramName) {
        if (value < min || value > max)
            return GeneralErrors.valueIsOutOfRange(paramName, value, min, max);
        return null;
    }

    public static Error againstOutOfRange(float value, float min, float max, String paramName) {
        if (value < min || value > max)
            return GeneralErrors.valueIsOutOfRange(paramName, value, min, max);
        return null;
    }

    public static Error againstOutOfRange(double value, double min, double max, String paramName) {
        if (value < min || value > max)
            return GeneralErrors.valueIsOutOfRange(paramName, value, min, max);
        return null;
    }

    public static Error againstOutOfRange(BigDecimal value, BigDecimal min, BigDecimal max, String paramName) {
        if (value == null || value.compareTo(min) < 0 || value.compareTo(max) > 0)
            return GeneralErrors.valueIsOutOfRange(paramName, value, min, max);
        return null;
    }
}