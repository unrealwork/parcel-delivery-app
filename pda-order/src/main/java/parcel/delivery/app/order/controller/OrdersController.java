package parcel.delivery.app.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.order.domain.OrderDto;
import parcel.delivery.app.order.service.OrderService;

import java.security.Principal;
import java.util.List;

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
}
