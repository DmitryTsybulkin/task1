package com.alfabattle.task1.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeAccess {

    private String mode;
    private String schedule;

}
