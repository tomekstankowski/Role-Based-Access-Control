package com.stankowski_strzelka.rbac.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Duty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User medical;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int office;


    public Duty(User medical, LocalDateTime startDate, LocalDateTime endDate, int office) {
        this.medical = medical;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
