package iuh.fit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_checkout")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryCheckOut  implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "room_history_checkout_id")
    private String roomHistoryCheckOutID;

    @Column(name = "date_checking_out", nullable = false)
    private LocalDateTime dateOfCheckingOut;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private ReservationForm reservationForm;
}
