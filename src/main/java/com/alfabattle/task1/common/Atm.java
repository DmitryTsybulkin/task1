package com.alfabattle.task1.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Set;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Atm {

    private Long deviceId;
    private Long recordUpdated;
    private Set<String> availablePaymentSystems;
    private Set<String> cashInCurrencies;
    private Set<String> cashOutCurrencies;
    private String publicAccess;
    private JsonService services;
    private Integer timeShift;
    private TimeAccess timeAccess;
    private Coordinates coordinates;
    private Address address;
    private String addressComments;
    private SupportInfo supportInfo;
    private String nfc;
}
