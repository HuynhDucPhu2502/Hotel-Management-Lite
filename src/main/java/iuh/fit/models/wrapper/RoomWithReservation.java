package iuh.fit.models.wrapper;

import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomWithReservation {
    private Room room;
    private ReservationForm reservationForm;

}
