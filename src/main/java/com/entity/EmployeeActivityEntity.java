package com.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "employee_activity")
@Data
@NoArgsConstructor
public class EmployeeActivityEntity {

    @Id
    private Long employee_id;

    @Column(name = "activity")
    @OneToMany(
            mappedBy = "employeeActivityEntity",
            cascade = CascadeType.ALL
    )
    private List<Activity> activities;
}
