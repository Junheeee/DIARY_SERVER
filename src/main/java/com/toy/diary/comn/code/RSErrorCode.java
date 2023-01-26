package com.toy.diary.comn.code;

public enum RSErrorCode {
    SUCCESS("1", "Success"),
    FAIL("-1" , "Fail"),
    ER_PARAM("-100" , "Please parameter Check"),
    DATA_NOTFOUNT("-101" , "Data not found"),
    PSWD_NOTMATCH("-102" , "Password does not match"),
    ACNT_UNAVAIL("-103","Account is unavailable"),

    INTERNAL_SERVER_ERROR("-500", "Internal system error"),
    DATA_DUPLICATE("-104","Duplicate data exists");


    private final String code;

    private final String message;

    private RSErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
