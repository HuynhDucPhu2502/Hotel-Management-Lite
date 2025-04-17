package iuh.fit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Admin 1/15/2025
 **/
@Entity
@Table(name = "invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Invoice {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "invoice_id", nullable = false)
    private String invoiceID;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "room_charges", nullable = false)
    private double roomCharges; // Tổng tiền phòng

    @Column(name = "service_charges", nullable = false)
    private double serviceCharges; // Tổng tiền dịch vụ

    @Column(name = "sub_total", nullable = false)
    private double subTotal; // Tổng tiền trước thuế = Tổng tiền phòng + dịch vụ

    @Column(name = "tax_charge", nullable = false)
    private double taxCharge; // Tiền thuế = Tổng tiền trước thuế * 0.1

    @Column(name = "total_due", nullable = false)
    private double totalDue; // Tổng tiền sau thuế = Tổng tiền trước thuế + tiền thuế;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationForm reservationForm;

}
