package com.smartorder.order.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderRequest {

    @NotEmpty(message = "order must contain atleast one item")
    private List<OrderItemRequest> items;

    public OrderRequest(List<OrderItemRequest> items) {
        this.items = items;
    }

    public OrderRequest() {
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
