package org.example.orderservice.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.dto.ShippingStatusEvent;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderShippingConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "shipping-events", groupId = "order-shipping-group")
    @Transactional
    public void consumeShippingEvent(ShippingStatusEvent event) {
        log.info("--> [Order-Service] Nhận sự kiện từ shipping-events: Đơn={}, Status={}",
                event.getOrderId(), event.getStatus());

        // Kiểm tra đúng trạng thái "DELIVERED"
        if ("DELIVERED".equalsIgnoreCase(event.getStatus())) {
            orderRepository.findByOrderId(event.getOrderId()).ifPresentOrElse(order -> {
                order.setStatus("COMPLETED");
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
                log.info("--> [DB SUCCESS] Đã cập nhật trạng thái đơn hàng {} thành COMPLETED!", order.getOrderId());
            }, () -> {
                log.warn("--> [DB WARN] Không tìm thấy đơn hàng {} trong database!", event.getOrderId());
            });
        }
    }
}