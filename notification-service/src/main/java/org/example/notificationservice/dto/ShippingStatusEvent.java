package org.example.notificationservice.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingStatusEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String trackingNumber;
    private String status; // "SHIPPED", "DELIVERING", "DELIVERED", "CANCELLED"
    private String customerEmail;
    private String deliveryAddress;
    private LocalDateTime updatedAt;
}
