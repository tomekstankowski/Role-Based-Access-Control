package com.stankowski_strzelka.rbac.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Appointment {

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", medical=" + medical +
                ", patient=" + patient +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User medical;

    @ManyToOne
    private User patient;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Appointment(User medical, User patient, LocalDateTime startDate) {
        this.medical = medical;
        this.patient = patient;
        this.startDate = startDate;
        this.endDate = startDate.plusMinutes(30);
    }

}
