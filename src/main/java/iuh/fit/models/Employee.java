package iuh.fit.models;

import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.Position;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Admin 1/14/2025
 **/
@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends Person {

    @Column(name = "employee_code", unique = true, nullable = false)
    private String employeeCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;


    public Employee(
            String employeeCode, String fullName, String phoneNumber, String address,
            Gender gender, String idCardNumber, LocalDate dob, ObjectStatus isActivate,
            Position position
    ) {
        super(null, fullName, phoneNumber, address, gender, idCardNumber, dob, isActivate);
        this.employeeCode = employeeCode;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeCode='" + employeeCode + '\'' +
                ", position=" + position +
                ", " + super.toString() +
                '}';
    }
}
