package parcel.delivery.app.delivery.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@Tag(name = "Delivery",
        description = "Endpoints related to delivery of orders")
public class DeliveryController {
    private final DelvieryService delvieryService;

    @Operation(summary = "View details",
            description = """
                    Retrieve information about delivery of the order. Behaviour is user role based:
                    <ul><li>User: own created orders</li>
                    <li>Courier: orders assigned to him</li></ul>
                    """,
            parameters = @Parameter(description = """
                    Unique identifier of the order.
                    """,
                    name = "orderId",
                    example = "4843a6c6-28e7-4a85-8f8d-f7e343389350"
            ),
            responses = {
                    @ApiResponse(
                            description = "In case if delivery for orderId is not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "In case if order is not accessible for a user",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/{orderId}")
    @AuthPolicy(VIEW_DELIVERY_DETAILS)
    public ResponseEntity<DeliveryDto> delivery(@PathVariable UUID orderId) {
        return ResponseEntity.ok(delvieryService.get(orderId));
    }

    @Operation(summary = "Assign courier",
            description = """
                    Allows to assign a courier user
                    """,
            parameters = @Parameter(description = """
                    Unique identifier of the order.
                    """,
                    name = "orderId",
                    example = "4843a6c6-28e7-4a85-8f8d-f7e343389350"
            ),
            responses = {
                    @ApiResponse
            }
    )
    @PutMapping("/{orderId}/assign")
    @AuthPolicy(ASSIGN_COURIER)
    public ResponseEntity<Void> assign(@PathVariable UUID orderId,
                                       @Valid @RequestBody AssignCourierRequest request) {
        delvieryService.assign(orderId, request);
        return ResponseEntity.noContent()
                .build();
    }


    @Operation(summary = "Track",
            description = """
                    Retrieves information about current location of the order
                    """,
            parameters = @Parameter(description = """
                    Unique identifier of the order.
                    """,
                    name = "orderId",
                    example = "4843a6c6-28e7-4a85-8f8d-f7e343389350"
            )
    )
    @GetMapping("/{orderId}/track")
    @AuthPolicy(RolePrivilege.TRACK_DELIVERY)
    public ResponseEntity<LongLat> track(@PathVariable UUID orderId) {
        LongLat longLat = delvieryService.track(orderId);
        return ResponseEntity.ok(longLat);
    }
}
