package com.smartorder.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "order_created_topic", groupId = "smartorder_group")
    public void consume(String message) {
        try {
            OrderCreatedEvent event = mapper.readValue(message, OrderCreatedEvent.class);
            System.out.println("Received event: " + event.getOrderId());
        } catch (Exception e) {
            System.err.println("Failed to deserialize message: " + e.getMessage());
        }
    }
}

