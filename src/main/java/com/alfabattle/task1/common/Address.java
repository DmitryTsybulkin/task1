package com.alfabattle.task1.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String mode;
    private String city;
    private String location;

}
