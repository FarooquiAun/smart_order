package com.smartorder.order;

import com.smartorder.kafka.OrderCreatedEvent;
import com.smartorder.kafka.OrderEventProducer;
import com.smartorder.order.dto.OrderItemRequest;
import com.smartorder.order.dto.OrderItemResponse;
import com.smartorder.order.dto.OrderRequest;
import com.smartorder.order.dto.OrderResponse;
import com.smartorder.product.Product;
import com.smartorder.product.ProductRepository;
import com.smartorder.security.model.User;
import com.smartorder.security.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderEventProducer eventProducer;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderEventProducer eventProducer) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.eventProducer = eventProducer;
    }

    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User customer=userRepository.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("User not found")
        );
        Order order=new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount=BigDecimal.ZERO;
        for (OrderItemRequest item:request.getItems()){
            Product product=productRepository.findById(item.getProductId()).orElseThrow(
                    ()-> new RuntimeException("Product not found exception")

            );
            BigDecimal unitPrice=product.getPrice();
            int quantity= item.getQuantity();
           BigDecimal lineTotal=unitPrice.multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(lineTotal);
            OrderItem orderItem=new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setLineTotal(lineTotal);
            orderItem.setUnitPrice(unitPrice);

            order.addItem(orderItem);


        }
        order.setTotalAmount(totalAmount);
        Order saveOrder=orderRepository.save(order);
        OrderCreatedEvent event=new OrderCreatedEvent();
        event.setOrderId(order.getId());
        event.setCustomerId(order.getCustomer().getId());
        event.setTotalAmount(order.getTotalAmount());

        com.smartorder.notification.event.OrderCreatedEvent event1=new com.smartorder.notification.event.OrderCreatedEvent();
        event1.setOrderId(saveOrder.getId());
        event1.setCustomerId(customer.getId());
        event1.setCustomerEmail(customer.getEmail());
        event1.setMessage("Order Event published successfully");
        eventProducer.sendOrderNotificationEvent(event1);
        List<OrderCreatedEvent.OrderItemEvent> itemEvents=order.getItems().stream()
                .map(i->{
                    OrderCreatedEvent.OrderItemEvent e=new OrderCreatedEvent.OrderItemEvent();
                    e.setProductId(i.getProduct().getId());
                    e.setQuantity(i.getQuantity());
                    e.setPrice(i.getUnitPrice());
                    return  e;
                }).toList();
       event.setItems(itemEvents);
       eventProducer.sendOrderEvent(event);
        return toDto(saveOrder);
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return toDto(order);
    }

    @Override
    public Page<OrderResponse> search(OrderStatus status, Instant createdFrom, Instant createdTo, BigDecimal minAmount, BigDecimal maxAmount, Long customerId,int page,
                                      int size) {
        Specification<Order> spec=OrderSpecification.withFilters(status,createdFrom,createdTo,minAmount,maxAmount,customerId);
         Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
         Page<Order> resultPage=orderRepository.findAll(spec,pageable);
         List<OrderResponse> content=resultPage.getContent().stream()
                 .map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(content,pageable,resultPage.getTotalElements());
    }
    private OrderResponse toDto(Order order){
        List<OrderItemResponse> responseList=order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getLineTotal()
                )).toList();
        return new OrderResponse(
                order.getId(),
                order.getCustomer().getUsername(),
                order.getStatus(),
                order.getTotalAmount(),
                responseList,
                order.getCreatedAt()

        );
    }
}
