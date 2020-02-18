package com.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RequestEmployeeActivity {
    private Long employee_id;
    private List<RequestActivity> activities;
}
