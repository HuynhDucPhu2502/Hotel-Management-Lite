package iuh.fit.models.wrapper;

import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomWithReservation  implements Serializable {
    private Room room;
    private ReservationForm reservationForm;

}
