package com.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Response {
    List<EmployeeStatistics> all_employees_last_7_days_statistics;
    List<EmployeeActivity> todays_activities;
}
