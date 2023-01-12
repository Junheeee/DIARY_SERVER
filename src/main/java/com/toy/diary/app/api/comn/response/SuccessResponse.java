package com.toy.diary.app.api.comn.response;

import lombok.Data;

import java.util.List;

@Data
public class SuccessResponse<T> extends BasicResponse {
    private int count;
    private T data;

    public SuccessResponse(T data) {
        this.data = data;
        if(data instanceof List) {
            this.count = ((List<?>)data).size();
        } else {
            this.count = 1;
        }
    }
}
