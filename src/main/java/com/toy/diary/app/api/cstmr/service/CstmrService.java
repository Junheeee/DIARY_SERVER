package com.toy.diary.app.api.cstmr.service;

import com.toy.diary.app.api.cstmr.model.CstmrModel;
import com.toy.diary.app.jpa.repository.CstmrBasQueryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CstmrService {

    @Autowired
    private CstmrBasQueryRepository query;

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
