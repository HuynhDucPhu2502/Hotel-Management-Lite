package iuh.fit.models;

import iuh.fit.models.enums.ShiftDaysSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Le Tran Gia Huy
 * @created 15/01/2025 - 9:06 AM
 * @project Hotel-Management-Plus
 * @package iuh.fit.models
 */

@Entity
@Table(name="shifts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Shift {

    @Id
    @Column(name = "shift_id", columnDefinition = "nvarchar(15)", nullable = false)
    @EqualsAndHashCode.Include
    private String shiftID;

    @Column(name="start_time", columnDefinition = "time", nullable = false)
    private LocalTime startTime;

    @Column(name="end_time", columnDefinition = "time", nullable = false)
    private LocalTime endTime;

    @Column(name = "shift_days_schedule", columnDefinition = "nvarchar(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShiftDaysSchedule shiftDaysSchedule;

    @Column(name = "modified_date", columnDefinition = "datetime", nullable = false)
    private LocalDateTime modifiedDate;

    @Column(name = "number_of_hour", columnDefinition = "int", nullable = false)
    private int numberOfHour;

    @ManyToMany
    @JoinTable(
            name = "shift_assignments",
            joinColumns = @JoinColumn(name = "shift_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @ToString.Exclude
    private Set<Employee> employees = new HashSet<>();
}
