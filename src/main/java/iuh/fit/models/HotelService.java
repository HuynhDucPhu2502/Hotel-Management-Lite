package iuh.fit.models;

import iuh.fit.models.enums.ObjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hotel_services")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelService {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "service_id")
    private String serviceID;

    @Column(name = "service_name", unique = true, nullable = false)
    private String serviceName;

    private String description;

    @Column(name = "service_price", nullable = false)
    private double servicePrice;

    @Column(nullable = false)
    private ObjectStatus isActivate;

    @ManyToOne()
    @JoinColumn(name = "service_category_id")
    private ServiceCategory serviceCategory;
}
