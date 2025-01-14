package iuh.fit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Admin 1/14/2025
 **/
@Entity
@Table(name = "reservation_forms")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationForm {
    @Id
    @Column(name = "reservation_id")
    private String reservationID;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Column(name = "approx_check_in_date", nullable = false)
    private LocalDateTime approxcheckInDate;

    @Column(name = "approx_check_out_date", nullable = false)
    private LocalDateTime approxcheckOutTime;

    @OneToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "customer_code", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_code", nullable = false)
    private Employee employee;
}
