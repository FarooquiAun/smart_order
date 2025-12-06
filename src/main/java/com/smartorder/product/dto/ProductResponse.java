package com.smartorder.product.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean active;
    private Instant createdAt;

    public ProductResponse(Long id, String name, String description, BigDecimal price, boolean active, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
        this.createdAt = createdAt;
    }

    public ProductResponse() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
