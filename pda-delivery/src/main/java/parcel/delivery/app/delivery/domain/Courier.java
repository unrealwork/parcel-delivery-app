package parcel.delivery.app.delivery.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Collection;

@Entity
@Table(name = "couriers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Courier {
    @Id
    private String userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourierStatus status;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;


    @OneToMany(mappedBy = "courier")
    private Collection<Delivery> deliveries;
}
