package parcel.delivery.app.delivery.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.common.annotations.AuthPolicy;
import parcel.delivery.app.delivery.domain.DeliveryDto;
import parcel.delivery.app.delivery.service.DelvieryService;

import java.util.UUID;

import static parcel.delivery.app.common.security.core.RolePrivilege.VIEW_DELIVERY_DETAILS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryController {
    private final DelvieryService delvieryService;

    @GetMapping("/{orderId}")
    @AuthPolicy(VIEW_DELIVERY_DETAILS)
    public ResponseEntity<DeliveryDto> delivery(@PathVariable UUID orderId) {
        return ResponseEntity.ok(delvieryService.get(orderId));
    }
}
