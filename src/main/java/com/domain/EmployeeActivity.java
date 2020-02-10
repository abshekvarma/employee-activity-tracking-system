package com.domain;

import com.entity.Activity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EmployeeActivity {
    private Long employee_id;
    private List<ActivityResponse> activities;
}
