package org.example.shippingservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.shippingservice.service.ShippingProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingProducerService shippingService;

    @PutMapping("/status")
    public ResponseEntity<String> updateStatus(
            @RequestParam String orderId,
            @RequestParam String trackingNumber,
            @RequestParam String status,
            @RequestParam(defaultValue = "khachhang@gmail.com") String email) {

        shippingService.updateAndPublishShippingStatus(orderId, trackingNumber, status, email);
        return ResponseEntity.ok("Cập nhật trạng thái vận chuyển thành công cho đơn: " + orderId);
    }
}