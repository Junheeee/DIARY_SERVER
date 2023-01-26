package com.toy.diary.app.api.jwt.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtRsModel implements Serializable {
    private String accessToken ;

    private String refreshToken;

    private String userId;

    private Integer cstmrSno;
}
