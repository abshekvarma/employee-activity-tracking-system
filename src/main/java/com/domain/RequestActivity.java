package com.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestActivity {
    private String name;
    private Long time;
    private int duration;
}
