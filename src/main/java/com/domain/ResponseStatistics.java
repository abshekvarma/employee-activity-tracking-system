package com.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseStatistics {
    private String activity_name;
    private Integer occurrences;
}
