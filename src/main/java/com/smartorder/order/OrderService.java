package com.smartorder.order;

import com.smartorder.order.dto.OrderRequest;
import com.smartorder.order.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest request);
    OrderResponse getOrderById(Long orderId);

    Page<OrderResponse> search(
         OrderStatus status,
         Instant createdFrom,
         Instant createdTo,
         BigDecimal minAmount,
         BigDecimal maxAmount,
         Long customerId,
         int page,
         int size
    );
}
