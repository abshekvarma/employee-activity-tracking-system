package com.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private EmployeeActivityEntity employeeActivityEntity;

    private String name;
    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private Date date;
}
