package com.toy.diary.comn.code;

public enum ErrorCode {
    SUCCESS("1", "성공"),
    FAIL("-1" , "실패"),
    INTERNAL_SERVER_ERROR("ER500", "내부 시스템 오류"),
    NOT_REGIST_ERROR_CODE("ER501", "등록되지 않은 오류 코드"),
    DATA_NOTFIND("DT001","데이터 를 찾을수 없습니다."),
    DATA_DUPLICATE("DT002","중복 데이터가 존재합니다."),
    DATA_NO("DT003", "데이터 미존재"),
    DB_ERROR("DB001" , "디비 처리중 오류"),
    PLAN_DATA_DUPLICATE("FT500", "이미 등록된 비행계획서의 비행구역과 비행시간이 일치합니다.\n비행시간 또는 비행구역을 수정하여 주십시오."),
    ARCRFT_DATA_DUPLICATE("FT500", "해당 기체는 다른 비행계획서에서 이미 등록된 기체입니다.\n비행시간 또는 기체 정보를 확인하여 주십시오.");



    private final String code;

    private final String message;

    private ErrorCode(String code, String message) {
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
