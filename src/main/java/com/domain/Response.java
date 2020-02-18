package com.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Response {
    List<ResponseStatistics> all_employees_last_7_days_statistics;
    List<ResponseActivity> todays_activities;
}
