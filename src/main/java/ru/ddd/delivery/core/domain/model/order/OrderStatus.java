package ru.ddd.delivery.core.domain.model.order;

public enum OrderStatus {
    CREATED, ASSIGNED, COMPLETED;

    public static OrderStatus fromValue(String value) {
        return OrderStatus.valueOf(value.toUpperCase());
    }

    public String toValue() {
        return name().toLowerCase();
    }
}
