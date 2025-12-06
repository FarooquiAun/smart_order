package com.smartorder.notification.consumer;

import com.smartorder.notification.event.OrderCreatedEvent;
import com.smartorder.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import tools.jackson.databind.ObjectMapper;

public class OrderNotificationConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public OrderNotificationConsumer(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "order_created_topic",
            groupId = "notification_group"
    )
    public void consumerOrderEvent(OrderCreatedEvent event){
        System.out.println("📥 [NOTIFICATION SERVICE] Consumed Order Event:");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("Customer ID: " + event.getCustomerId());
        System.out.println("Email: " + event.getCustomerEmail());

        notificationService.sendOrderConfirmation(event);
    }

}
