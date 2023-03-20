package parcel.delivery.app.delivery.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.common.annotations.AuthPolicy;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.delivery.controller.api.request.AssignCourierRequest;
import parcel.delivery.app.delivery.dto.DeliveryDto;
import parcel.delivery.app.delivery.dto.LongLat;
import parcel.delivery.app.delivery.service.DelvieryService;

import java.util.UUID;

import static parcel.delivery.app.common.security.core.RolePrivilege.ASSIGN_COURIER;
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

    @PutMapping("/{orderId}/assign")
    @AuthPolicy(ASSIGN_COURIER)
    public ResponseEntity<Void> assign(@PathVariable UUID orderId,
                                       @Valid @RequestBody AssignCourierRequest request) {
        delvieryService.assign(orderId, request);
        return ResponseEntity.noContent()
                .build();
    }


    @GetMapping("/{orderId}/track")
    @AuthPolicy(RolePrivilege.TRACK_DELIVERY)
    public ResponseEntity<LongLat> track(@PathVariable UUID orderId) {
        LongLat longLat = delvieryService.track(orderId);
        return ResponseEntity.ok(longLat);
    }
}
