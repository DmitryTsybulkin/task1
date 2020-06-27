package com.alfabattle.task1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtmResponse {

    private String city;
    private Long deviceId;
    private String latitude;
    private String location;
    private String longitude;
    private Boolean payments;

}
