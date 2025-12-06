package com.smartorder.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductRequest {
    @NotBlank
    @Size(min = 2,max = 500)
    private String name;
    @Size(max = 500)
    private String description;
    @NotNull
    @Min(0)
    private BigDecimal price;

    private Boolean active;

    public ProductRequest(Boolean active, String description, String name, BigDecimal price) {
        this.active = active;
        this.description = description;
        this.name = name;
        this.price = price;
    }

    public ProductRequest() {
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
