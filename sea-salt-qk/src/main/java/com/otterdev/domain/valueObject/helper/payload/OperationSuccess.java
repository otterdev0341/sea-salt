package com.otterdev.domain.valueObject.helper.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationSuccess {
    private String message;
}
