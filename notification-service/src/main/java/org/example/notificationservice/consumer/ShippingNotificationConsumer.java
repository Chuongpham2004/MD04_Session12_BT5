package org.example.notificationservice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.dto.ShippingStatusEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShippingNotificationConsumer {

    @KafkaListener(topics = "shipping-events", groupId = "notification-shipping-group")
    public void consumeShippingEvent(ShippingStatusEvent event) {
        log.info("--> [Notification-Service] Nhận sự kiện từ shipping-events: Đơn={}, Status={}",
                event.getOrderId(), event.getStatus());

        if ("DELIVERED".equalsIgnoreCase(event.getStatus())) {
            String email = event.getCustomerEmail() != null ? event.getCustomerEmail() : "khách hàng";
            log.info("🎉 [Notification] Gửi tin chúc mừng tới {}: "
                            + "Đơn hàng {} đã giao thành công! Cảm ơn bạn đã mua sắm.",
                    email, event.getOrderId());
        }
    }
}
