package com.smartorder.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrderEvent(OrderCreatedEvent event) {
        try {
            String jsonEvent = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order_created_topic", jsonEvent);
            System.out.println("Kafka event published successfully: " + event.getOrderId());
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize event: " + e.getMessage());
        }
    }

    public void sendOrderNotificationEvent(com.smartorder.notification.event.OrderCreatedEvent event) {
        try {
            String jsonEvent = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order_created_topic", jsonEvent);
            System.out.println("Kafka event published successfully: " + event.getOrderId());
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize event: " + e.getMessage());
        }
    }
}