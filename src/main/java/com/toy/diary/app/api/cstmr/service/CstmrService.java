package com.toy.diary.app.api.cstmr.service;

import com.toy.diary.app.api.cstmr.model.CstmrModel;
import com.toy.diary.app.jpa.repository.CstmrBasRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class CstmrService {

    @Autowired
    private CstmrBasRepository query;

    public CstmrModel login(CstmrModel rq) {
        CstmrModel isId = query.login(rq);
        int result;
        if(isId != null) {
            result = 200;
        } else {
            result = 0;
        }

        return isId;
    }


}
