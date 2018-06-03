package com.stankowski_strzelka.rbac.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User medical;

    @ManyToOne
    private User patient;

    private LocalDateTime startDate;
    private LocalDateTime endDate;


    public Appointment(User medical, User patient, LocalDateTime startDate, LocalDateTime endDate) {
        this.medical = medical;
        this.patient = patient;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
