package iuh.fit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Admin 4/4/2025
 **/
@Entity
@Table(name = "global_sequences")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GlobalSequence  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tableName;
    private String nextID;
}
