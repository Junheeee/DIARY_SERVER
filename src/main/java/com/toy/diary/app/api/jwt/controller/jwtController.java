package com.toy.diary.app.api.jwt.controller;

import com.toy.diary.app.api.comn.response.BasicResponse;
import com.toy.diary.app.api.comn.response.ErrorResponse;
import com.toy.diary.app.api.comn.response.SuccessResponse;
import com.toy.diary.app.api.jwt.model.JwtProfileRsModel;
import com.toy.diary.app.api.jwt.model.JwtRqModel;
import com.toy.diary.app.api.jwt.model.JwtRsModel;
import com.toy.diary.app.api.jwt.service.JwtService;
import com.toy.diary.comn.code.RSErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "/api/jwt", produces = {MediaType.APPLICATION_JSON_VALUE})
public class jwtController {

    @Autowired
    private JwtService service;

    @PostMapping(value = "/login")
    public ResponseEntity<?> loginProcess(@RequestBody JwtRqModel rq) throws Exception {
        Map<String, Object> resultMap = service.loginProcess(rq);

        int loginError = (int) resultMap.get("loginError");

        if(loginError < 0) {
            String errorMessage = (String) resultMap.get("errorMessage");
            return ResponseEntity.status(HttpStatus.OK).body(new ErrorResponse(errorMessage, loginError + ""));
        } else {
            JwtRsModel result = (JwtRsModel) resultMap.get("result");
            return ResponseEntity.ok().body(new SuccessResponse<JwtRsModel>(result));
        }
    }

    @GetMapping(value = "/kakaoLogin")
    public ResponseEntity<?> kakaoLogin(@RequestParam("token") String token)  {
        JwtRsModel result = new JwtRsModel();
        try{
            Map<String, Object> resultMap = service.kakaoLogin(token);
            result = (JwtRsModel) resultMap.get("result");
        } catch(Exception e) {
            log.error("IGONE : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server Error", "-1"));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(result));
    }

    @GetMapping(value = "profile/{cstmrSno}")
    public ResponseEntity<?> proflie(@PathVariable Integer cstmrSno) throws Exception{

        JwtProfileRsModel result = service.profile(cstmrSno);
        if(result == null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ErrorResponse(RSErrorCode.DATA_NOTFOUNT));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(result));
    }

    @GetMapping(value = "/kakaoLogout")
    public ResponseEntity<?> logout(@RequestParam("token") String token) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        URL url = new URL("https://kapi.kakao.com/v1/user/logout");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestProperty("Authorization", "Bearer " + token);
        urlConnection.setRequestMethod("POST");

        int result = urlConnection.getResponseCode();
        resultMap.put("code", result);

        log.info("response code: " + result);

        return ResponseEntity.ok().body(new SuccessResponse<>(resultMap));
    }

    @GetMapping(value = "/kakaoUnlink")
    public ResponseEntity<?> unlink(@RequestParam("token") String token) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            int result = service.kakaoUnlink(token);
            resultMap.put("code", result);
        } catch(Exception e) {
            log.error("IGONE : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server Error", "-1"));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(resultMap));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody JwtRqModel rq) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            boolean result = service.register(rq);
            resultMap.put("code", result);
        } catch(Exception e) {
            log.error("IGONE : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server Error", "-1"));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(resultMap));
    }

}
