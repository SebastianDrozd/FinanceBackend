package com.srankoin.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyTokenMessage {

    private String status;
    private String username;
}
