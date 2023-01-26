package com.toy.diary.app.api.jwt.controller;

import com.toy.diary.app.api.comn.response.BasicResponse;
import com.toy.diary.app.api.comn.response.ErrorResponse;
import com.toy.diary.app.api.comn.response.SuccessResponse;
import com.toy.diary.app.api.jwt.model.JwtRqModel;
import com.toy.diary.app.api.jwt.model.JwtRsModel;
import com.toy.diary.app.api.jwt.service.JwtService;
import com.toy.diary.app.api.jwt.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "/api/jwt", produces = {MediaType.APPLICATION_JSON_VALUE})
public class jwtController {

    @Autowired
    private JwtService service;

    @PostMapping(value = "/login")
    public ResponseEntity<? extends BasicResponse> createAuthenticationToken(@RequestBody JwtRqModel authenticationRequest) throws Exception {
        Map<String, Object> resultMap = service.loginProcess(authenticationRequest);

        int loginError = (int) resultMap.get("loginError");

        if(loginError < 0) {
            String errorMessage = (String) resultMap.get("errorMessage");
            return ResponseEntity.status(HttpStatus.OK).body(new ErrorResponse(errorMessage, loginError + ""));
        } else {
            JwtRsModel result = (JwtRsModel) resultMap.get("result");
            return ResponseEntity.ok().body(new SuccessResponse<JwtRsModel>(result));
        }
    }
}
