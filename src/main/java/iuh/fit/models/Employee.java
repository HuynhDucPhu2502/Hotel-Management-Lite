package iuh.fit.models;

import iuh.fit.models.enums.Position;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private Set<ShiftAssignment>  shiftAssignments = new HashSet<>();

    @Override
    public String toString() {
        return "Employee{" +
                "employeeCode='" + employeeCode + '\'' +
                ", position=" + position +
                ", " + super.toString() +
                '}';
    }
}
