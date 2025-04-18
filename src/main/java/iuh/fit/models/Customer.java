package iuh.fit.models;

import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Admin 1/14/2025
 **/
@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends Person implements Serializable {

    @Column(name = "customer_code", unique = true, nullable = false)
    private String customerCode;

    public Customer(
            String customerCode, String fullName, String phoneNumber, String address,
            Gender gender, String idCardNumber, LocalDate dob, ObjectStatus isActivate
    ) {
        super(null, fullName, phoneNumber, address, gender, idCardNumber, dob, isActivate);
        this.customerCode = customerCode;
    }


    @Override
    public String toString() {
        return  "Customer{" +
                "customerCode='" + customerCode + '\'' +
                ", " + super.toString() +
                '}';
    }
}
