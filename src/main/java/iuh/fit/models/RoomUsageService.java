package iuh.fit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "room_usage_services")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomUsageService {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "room_usage_id")
    private String roomUsageServiceID;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "day_added", nullable = false)
    private LocalDateTime dayAdded;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private HotelService hotelService;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private ReservationForm reservationForm;
}

