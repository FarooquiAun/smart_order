package com.smartorder.notification.service;


import com.smartorder.notification.event.OrderCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendOrderConfirmation(OrderCreatedEvent event){
        System.out.println("📧 Sending Email to: " + event.getCustomerEmail());
        System.out.println("📱 Sending SMS to customerId: " + event.getCustomerId());
        System.out.println("Order Message: " + event.getMessage());
    }
}
