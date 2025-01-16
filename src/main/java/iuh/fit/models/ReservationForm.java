package iuh.fit.models;

import iuh.fit.models.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

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


    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false)
    private ReservationStatus reservationStatus;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "customer_code", referencedColumnName = "customer_code", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_code", referencedColumnName = "employee_code", nullable = false)
    private Employee employee;

    @OneToOne(
            mappedBy = "reservationForm",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            },
            orphanRemoval = true
    )
    private HistoryCheckOut historyCheckOut;

    @OneToOne(
            mappedBy = "reservationForm",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            },
            orphanRemoval = true
    )
    private HistoryCheckIn historyCheckIn;

    @OneToMany(mappedBy = "reservationForm")
    private Set<RoomUsageService> roomUsageService;

    @Override
    public String toString() {
        return "ReservationForm{" +
                "reservationID='" + reservationID + '\'' +
                ", reservationDate=" + reservationDate +
                ", approxcheckInDate=" + approxcheckInDate +
                ", approxcheckOutTime=" + approxcheckOutTime +
                ", reservationStatus=" + reservationStatus +
                '}';
    }
}
