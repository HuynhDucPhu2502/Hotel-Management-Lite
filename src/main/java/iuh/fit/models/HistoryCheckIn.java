package iuh.fit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.JoinColumnOrFormula;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_checkin")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryCheckIn  implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "room_history_checkin_id")
    private String roomHistoryCheckinID;

    @Column(name = "checkin_date")
    private LocalDateTime checkInDate;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private ReservationForm reservationForm;
}
