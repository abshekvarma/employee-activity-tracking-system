package com.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActivityResponse {
    private String name;
    private Long start_time;
}
