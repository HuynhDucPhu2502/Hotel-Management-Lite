package iuh.fit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Admin 1/15/2025
 **/
@Entity
@Table(name = "reservation_room_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationRoomDetail implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "reservation_room_detail_id")
    private String reservationRoomDetailID;

    @Column(name = "date_changed", nullable = false)
    private LocalDateTime dateChanged;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationForm reservationForm;
}
