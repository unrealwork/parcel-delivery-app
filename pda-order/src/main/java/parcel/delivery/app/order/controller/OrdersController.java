package parcel.delivery.app.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.common.annotations.AuthPolicy;
import parcel.delivery.app.order.controller.api.request.ChangeOrderDestinationRequest;
import parcel.delivery.app.order.controller.api.request.ChangeStatusRequest;
import parcel.delivery.app.order.controller.api.request.CreateOrderRequest;
import parcel.delivery.app.order.dto.OrderDto;
import parcel.delivery.app.order.error.exception.OrderCancellationException;
import parcel.delivery.app.order.error.exception.OrderDestinationModificationException;
import parcel.delivery.app.order.error.exception.OrderNotFoundException;
import parcel.delivery.app.order.service.OrderService;

import java.util.List;
import java.util.UUID;

import static parcel.delivery.app.common.security.core.RolePrivilege.CANCEL_ORDER;
import static parcel.delivery.app.common.security.core.RolePrivilege.CHANGE_DESTINATION;
import static parcel.delivery.app.common.security.core.RolePrivilege.CHANGE_ORDER_STATUS;
import static parcel.delivery.app.common.security.core.RolePrivilege.CREATE_ORDER;
import static parcel.delivery.app.common.security.core.RolePrivilege.VIEW_ORDERS;

@RestController()
@RequestMapping("orders")
@RequiredArgsConstructor
@Tag(name = "Order API",
        description = "Endpoints related to users' orders")
public class OrdersController {
    private final OrderService orderService;


    @Operation(summary = "View",
            description = """
                    Retrieve information of orders accessible for an account depends on role.
                    <ul><li>User: own created orders</li>
                    <li>Admin: all orders</li>
                    <li>Courier: orders assigned to him</li></ul>
                    """
    )
    @GetMapping
    @AuthPolicy(VIEW_ORDERS)
    public ResponseEntity<List<OrderDto>> orders() {
        List<OrderDto> orders = orderService.orders();
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Create",
            description = "Allows users create users a new order."
    )
    @PostMapping
    @AuthPolicy(CREATE_ORDER)
    public ResponseEntity<OrderDto> create(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        return ResponseEntity.ok(orderService.create(createOrderRequest));
    }


    @Operation(summary = "Change status",
            description = """
                    Allows to change an order's status.<br/>
                    <ul>
                    <li>Admin - any of statuses</li>
                    <li>Courier - can change from PENDING -> IN_PROGRESS and IN_PROGRESS -> DELIVERED</li>
                    </ul>
                    """,
            parameters = @Parameter(description = """
                    Unique identifier of the order.
                    """,
                    name = "id",
                    example = "4843a6c6-28e7-4a85-8f8d-f7e343389350"
            ),
            responses = {
                    @ApiResponse(description = "In case of successful modification", responseCode = "204")
            }
    )
    @PutMapping("/{id}/status")
    @AuthPolicy(CHANGE_ORDER_STATUS)
    public ResponseEntity<Void> changeStatus(@PathVariable UUID id, @Valid @RequestBody ChangeStatusRequest changeStatusRequest) throws

            OrderNotFoundException {
        orderService.changeStatus(id, changeStatusRequest);
        return ResponseEntity.noContent()
                .build();
    }


    @Operation(summary = "Cancel",
            description = "Allows users to cancel their own orders. If order is already IN_PROGRESS cancellation is not possible.",
            parameters = @Parameter(description = """
                    Unique identifier of the order.
                    """,
                    name = "id",
                    example = "4843a6c6-28e7-4a85-8f8d-f7e343389350"
            ),
            responses = {
                    @ApiResponse(description = "In case of successful cancellation", responseCode = "204"),
                    @ApiResponse(description = "In case of cancellation of someone's else order", responseCode = "403"),
                    @ApiResponse(description = "In case of non existing  order", responseCode = "404"),
            }
    )
    @PutMapping("/{id}/cancel")
    @AuthPolicy(CANCEL_ORDER)
    public ResponseEntity<Void> cancel(@PathVariable UUID id) throws
            OrderNotFoundException, OrderCancellationException {
        orderService.cancel(id);
        return ResponseEntity.noContent()
                .build();
    }


    @Operation(summary = "Change destination",
            description = """
                    Allows to change an order's destination of own orders. Destination can be changed before moment when delivery is started.
                    """,
            parameters = @Parameter(description = """
                    Unique identifier of the order.
                    """,
                    name = "id",
                    example = "4843a6c6-28e7-4a85-8f8d-f7e343389350"
            ),
            responses = {
                    @ApiResponse(description = "In case of successful modification", responseCode = "204"),
                    @ApiResponse(description = "In case of modification of someone's else order", responseCode = "403"),

            }
    )
    @PutMapping("/{id}/destination")
    @AuthPolicy(CHANGE_DESTINATION)
    public ResponseEntity<Void> destination(@PathVariable UUID id,
                                            @Valid @RequestBody ChangeOrderDestinationRequest destinationRequest) throws
            OrderNotFoundException, OrderDestinationModificationException {
        orderService.changeDestination(id, destinationRequest);
        return ResponseEntity.noContent()
                .build();
    }
}
