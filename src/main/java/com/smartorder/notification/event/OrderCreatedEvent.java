package com.smartorder.notification.event;

public class OrderCreatedEvent {
    private Long orderId;
    private Long customerId;
    private String customerEmail;
    private String message;

    public OrderCreatedEvent(String customerEmail, Long customerId, String message, Long orderId) {
        this.customerEmail = customerEmail;
        this.customerId = customerId;
        this.message = message;
        this.orderId = orderId;
    }

    public OrderCreatedEvent() {
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
