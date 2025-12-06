package com.smartorder.order;

import com.smartorder.security.model.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User customer;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> items=new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private OrderStatus status= OrderStatus.PENDING;

    @Column(nullable = false,updatable = false)
    private Instant createdAt= Instant.now();
    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public Order(Instant createdAt, User customer, Long id, List<OrderItem> items, OrderStatus status,BigDecimal totalAmount) {
        this.createdAt = createdAt;
        this.customer = customer;
        this.id = id;
        this.items = items;
        this.status = status;
        this.totalAmount=totalAmount;
    }

    public Order() {
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
    public void removeItem(OrderItem item){
        items.remove(item);
        item.setOrder(null);
    }
}
