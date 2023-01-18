package com.toy.diary.app.api.cstmr.controller;

import com.toy.diary.app.api.comn.response.BasicResponse;
import com.toy.diary.app.api.comn.response.ErrorResponse;
import com.toy.diary.app.api.comn.response.SuccessResponse;
import com.toy.diary.app.api.cstmr.model.CstmrModel;
import com.toy.diary.app.api.cstmr.service.CstmrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/cstmr", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CstmrController {

    private final CstmrService service;

    @PostMapping(value = "/login")
    public ResponseEntity<? extends BasicResponse> login(@RequestBody CstmrModel rq) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        CstmrModel result = null;
        if(rq == null || rq.getUserId() == null || rq.getUserPswd() == null) {
//            resultMap.put("result", null);
            result = null;
        } else {

        try{
//            resultMap.put("result", service.login(rq));
            result = service.login(rq);
        } catch(Exception e) {
            log.error("IGNORE : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server Error", "-1"));
        }
        }

        return ResponseEntity.ok().body(new SuccessResponse<>(result));
    }

}
