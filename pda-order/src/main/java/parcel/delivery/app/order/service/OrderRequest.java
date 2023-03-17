package parcel.delivery.app.order.service;

import java.util.UUID;

public record OrderRequest<T>(UUID id, T req) {
}
