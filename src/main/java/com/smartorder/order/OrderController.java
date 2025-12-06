package com.smartorder.order;

import com.smartorder.order.dto.OrderRequest;
import com.smartorder.order.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest request){
        OrderResponse response=orderService.placeOrder(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse>  getOrder(@PathVariable Long id){
        OrderResponse response=orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<OrderResponse>> searchOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Instant createdFrom,
            @RequestParam(required = false) Instant createdTo,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {


        Page<OrderResponse> result = orderService.search(
                status,
                createdFrom,
                createdTo,
                minAmount,
                maxAmount,
                customerId,
                page,
                size
        );

        return ResponseEntity.ok(result);
    }
}
