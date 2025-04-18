package iuh.fit.models;

import iuh.fit.models.enums.ObjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 14/01/2025 - 9:46 AM
 * @project Hotel-Management-Plus
 * @package iuh.fit.models
 */

@Entity
@Table(name = "room_categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RoomCategory implements Serializable {

    @Id
    @Column(name = "room_category_id", columnDefinition = "nvarchar(50)", nullable = false)
    @EqualsAndHashCode.Include
    private String roomCategoryID;

    @Column(name = "room_category_name", columnDefinition = "nvarchar(50)", nullable = false)
    private String roomCategoryName;

    @Column(name = "number_of_bed", nullable = false)
    private int numberOfBed;

    @Column(name = "hourly_price", nullable = false)
    private double hourlyPrice;

    @Column(name = "daily_price", nullable = false)
    private double dailyPrice;

    @Column(name = "is_activate", columnDefinition = "nvarchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private ObjectStatus isActivate;

    @OneToMany(mappedBy = "roomCategory")
    @ToString.Exclude
    private List<Room> rooms;

    @Override
    public String toString() {
        return roomCategoryID + ' ' + roomCategoryName;
    }

}
