package parcel.delivery.app.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Table(name = "orders")
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 2039569510227677652L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private String createdBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;
}
