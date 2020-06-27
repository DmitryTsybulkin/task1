package com.alfabattle.task1.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Set;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Data {

    private String bankLicense;
    private Set<Atm> atms;

}
