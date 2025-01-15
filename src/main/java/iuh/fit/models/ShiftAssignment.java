package iuh.fit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Le Tran Gia Huy
 * @created 15/01/2025 - 9:45 AM
 * @project Hotel-Management-Plus
 * @package iuh.fit.models
 */

@Entity
@Table(name = "shift_assignments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShiftAssignment {

    @Id
    @Column(name = "shift_assignment_id", columnDefinition = "nvarchar(15)", nullable = false)
    @EqualsAndHashCode.Include
    private String shiftAssignmentID;

    @ManyToOne
    @JoinColumn(name = "shift_id", referencedColumnName = "shift_id", nullable = false)
    private Shift shift;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_code", nullable = false)
    private Employee employee;

    @Column(name = "description", columnDefinition = "nvarchar(255)", nullable = false)
    private String description;
}
