package com.toy.diary.app.api.jwt.model;

import lombok.Data;

@Data
public class JwtProfileRsModel {
    private int cstmrSno;

    private String userId;

    private String memberNm;
}
