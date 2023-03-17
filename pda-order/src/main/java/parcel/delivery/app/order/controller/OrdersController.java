package parcel.delivery.app.order.controller;

import jakarta.servlet.http.HttpServletRequest;
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
import parcel.delivery.app.order.controller.api.request.ChangeOrderDestinationRequest;
import parcel.delivery.app.order.controller.api.request.ChangeStatusRequest;
import parcel.delivery.app.order.controller.api.request.CreateOrderRequest;
import parcel.delivery.app.order.dto.OrderDto;
import parcel.delivery.app.order.error.exception.OrderCancellationException;
import parcel.delivery.app.order.error.exception.OrderDestinationModificationException;
import parcel.delivery.app.order.error.exception.OrderNotFoundException;
import parcel.delivery.app.order.service.OrderService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> orders(HttpServletRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        List<OrderDto> orders = orderService.ordersForUser(userPrincipal.getName());
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        return ResponseEntity.ok(orderService.create(createOrderRequest));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable UUID id, @Valid @RequestBody ChangeStatusRequest changeStatusRequest) throws OrderNotFoundException {
        orderService.changeStatus(id, changeStatusRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) throws
            OrderNotFoundException, OrderCancellationException {
        orderService.cancel(id);
        return ResponseEntity.noContent()
                .build();
    }


    @PutMapping("/{id}/destination")
    public ResponseEntity<Void> cancel(@PathVariable UUID id,
                                       @Valid @RequestBody ChangeOrderDestinationRequest destinationRequest) throws
            OrderNotFoundException, OrderDestinationModificationException {
        orderService.changeDestination(id, destinationRequest);
        return ResponseEntity.noContent()
                .build();
    }
}
