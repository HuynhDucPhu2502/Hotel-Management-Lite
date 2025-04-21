package iuh.fit.models;

import iuh.fit.models.enums.ObjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "service_categories")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCategory  implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "service_category_id")
    private String serviceCategoryID;

    @Column(name = "service_category_name", unique = true, nullable = false, columnDefinition = "nvarchar(50)")
    private String serviceCategoryName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObjectStatus isActivate;

    @Column(nullable = false)
    private String icon;
}
