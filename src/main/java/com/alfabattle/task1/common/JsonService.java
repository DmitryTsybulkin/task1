package com.alfabattle.task1.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonService {

    private String cardCashOut;
    private String cardCashIn;
    private String cashOut;
    private String cashIn;
    private String cardPayments;
    private String payments;


}
