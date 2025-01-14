package iuh.fit.models;

import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Le Tran Gia Huy
 * @created 14/01/2025 - 9:46 AM
 * @project Hotel-Management-Plus
 * @package iuh.fit.models
 */

@Entity
@Table(name = "rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Room {
    @Id
    @Column(name = "room_id", columnDefinition = "nvarchar(15)", nullable = false)
    @EqualsAndHashCode.Include
    private String roomID;

    @Column(name = "room_status", columnDefinition = "nvarchar(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    @Column(name = "date_of_creation", nullable = false)
    private LocalDateTime dateOfCreation;

    @Column(name = "is_activate", columnDefinition = "nvarchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    private ObjectStatus isActivate;

    @ManyToOne
    @JoinColumn(name = "room_category_id", nullable = false)
    @ToString.Exclude
    private RoomCategory roomCategory;
}
