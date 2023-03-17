package parcel.delivery.app.order.service;

import parcel.delivery.app.order.controller.api.request.ChangeOrderDestinationRequest;
import parcel.delivery.app.order.controller.api.request.ChangeStatusRequest;
import parcel.delivery.app.order.controller.api.request.CreateOrderRequest;
import parcel.delivery.app.order.dto.OrderDto;
import parcel.delivery.app.order.error.exception.OrderCancellationException;
import parcel.delivery.app.order.error.exception.OrderDestinationModificationException;
import parcel.delivery.app.order.error.exception.OrderNotFoundException;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> orders();

    OrderDto create(CreateOrderRequest order);

    void changeStatus(UUID id, ChangeStatusRequest changeStatusRequest) throws OrderNotFoundException;

    void cancel(UUID id) throws OrderNotFoundException, OrderCancellationException;

    void changeDestination(UUID id, ChangeOrderDestinationRequest destinationRequest) throws OrderNotFoundException, OrderDestinationModificationException;
}
