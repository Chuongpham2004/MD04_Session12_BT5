package org.example.shippingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shippingservice.dto.ShippingStatusEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingProducerService {

    private static final String TOPIC = "shipping-events";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void updateAndPublishShippingStatus(String orderId, String trackingNumber, String status, String email) {
        ShippingStatusEvent event = ShippingStatusEvent.builder()
                .orderId(orderId)
                .trackingNumber(trackingNumber)
                .status(status)
                .customerEmail(email)
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("==> [Shipping-Service] Publish sự kiện: Mã đơn={}, Trạng thái={} lên topic '{}'",
                orderId, status, TOPIC);

        kafkaTemplate.send(TOPIC, orderId, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("==> [Kafka OK] Đã gửi ShippingStatusEvent cho đơn {} (partition={}, offset={})",
                                orderId,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("==> [Kafka ERROR] Gửi sự kiện thất bại cho đơn {}: {}", orderId, ex.getMessage());
                    }
                });
    }
}
