package com.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employeeActivityEntity")
@Data
@NoArgsConstructor
public class EmployeeActivityEntity {

    @Id
    private Long employee_id;

    @Column(name = "activities")
    @OneToMany(
            mappedBy = "employeeActivityEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @Fetch(FetchMode.SELECT)
    private List<Activity> activities = new ArrayList<>();

    public void addActivities(Activity activity) {
        this.activities.add(activity);
        activity.setEmployeeActivityEntity(this);
    }
}
