package com.toy.diary.app.api.kakao.controller;

import com.toy.diary.app.api.comn.response.ErrorResponse;
import com.toy.diary.app.api.comn.response.SuccessResponse;
import com.toy.diary.app.api.kakao.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/kakao")
public class KakaoController {

    @Autowired
    private KakaoService kakaoService;

    @GetMapping("/login")
    public ResponseEntity<?> realLogin(@RequestParam("token") String token) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            resultMap = kakaoService.login(token);
        } catch(Exception e) {
            log.error("IGONE : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server Error", "-1"));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(resultMap));
    }

    @GetMapping("/logout")
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

    @GetMapping("/unlink")
    public ResponseEntity<?> unlink(@RequestParam("token") String token) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            int result = kakaoService.unlink(token);
            resultMap.put("code", result);
        } catch(Exception e) {
            log.error("IGONE : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server Error", "-1"));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(resultMap));
    }
}
