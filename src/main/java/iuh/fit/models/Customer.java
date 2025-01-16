package iuh.fit.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Admin 1/14/2025
 **/
@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends Person {

    @Column(name = "customer_code", unique = true, nullable = false)
    private String customerCode;

    @Override
    public String toString() {
        return  "Customer{" +
                "customerCode='" + customerCode + '\'' +
                super.toString() +
                '}';
    }
}
