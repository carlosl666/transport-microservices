package com.transport.order.entity;

public enum OrderStatus {
    CREATED,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED;

    public boolean validSts(OrderStatus orderStatus) {
        return switch (this) {
            case CREATED -> orderStatus == IN_TRANSIT || orderStatus == CANCELLED;
            case IN_TRANSIT -> orderStatus == DELIVERED || orderStatus == CANCELLED;
            case DELIVERED, CANCELLED -> false;
        };
    }
}
