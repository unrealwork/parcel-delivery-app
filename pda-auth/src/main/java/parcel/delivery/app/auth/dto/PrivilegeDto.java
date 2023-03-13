package parcel.delivery.app.auth.dto;

import parcel.delivery.app.auth.domain.Privilege;

import java.io.Serializable;

/**
 * A DTO for the {@link Privilege} entity
 */
public record PrivilegeDto(Long id, String name) implements Serializable {
}
