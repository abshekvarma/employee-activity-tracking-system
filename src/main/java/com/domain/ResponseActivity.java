package com.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponseActivity {
    private Long employee_id;
    private List<ActivityDomain> activities;
}
