package com.alfabattle.task1.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class General {

    private com.alfabattle.task1.common.Data data;
    private Integer total;
    private String success;
    private Object error;

}
