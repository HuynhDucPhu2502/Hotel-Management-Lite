package iuh.fit.dto;

import iuh.fit.models.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;

import java.time.LocalDateTime;

/**
 * @author Le Tran Gia Huy
 * @created 18/04/2025 - 2:44 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dto
 */

@Getter
@Setter
@NoArgsConstructor
public class InvoiceInfoDTO {
    private Customer customer;
    private String invoiceID;
    private String roomID;
    private String roomCategory;
    private double totalDue;
    private LocalDateTime invoiceDate;

    public InvoiceInfoDTO(Customer customer, String invoiceID, String roomID, String roomCategory, double totalDue, LocalDateTime invoiceDate) {
        this.customer = customer;
        this.invoiceID = invoiceID;
        this.roomID = roomID;
        this.roomCategory = roomCategory;
        this.totalDue = totalDue;
        this.invoiceDate = invoiceDate;
    }

    // Getters, Setters (nếu cần)
}
