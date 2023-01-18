package com.toy.diary.app.api.kakao.service;

import com.toy.diary.app.jpa.entity.CstmrBas;
import com.toy.diary.app.jpa.entity.CstrmDtl;
import com.toy.diary.app.jpa.repository.KakaoRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoService {

    @Autowired
    private KakaoRepository query;

    public Map<String, Object> login(String token) throws IOException {
        String host = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> userInfo = new HashMap<>();
        try{
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while( ( line = br.readLine() ) != null ) {
                res += line;
            }

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(res);
            JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
            JSONObject properties = (JSONObject) obj.get("properties");

            String id = obj.get("id").toString();
            String nickname = properties.get("nickname").toString();
//            String email = properties.get("")

            userInfo.put("id", id);
            userInfo.put("nickname", nickname);

            CstmrBas bas = new CstmrBas();
            CstrmDtl dtl = new CstrmDtl();

            Long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);

            bas.setUseYn("Y");
            bas.setUserId(id);
            bas.setJoinDt(timestamp);
            dtl.setMemberNm(nickname);

            boolean checkResult = query.kakaoUserCheck(id);

            if(checkResult == true) {
//            kakaoUserDataDtlRepository.save(setData);
            } else {
                System.out.println("이미 가입된 회원입니다");
            }

            br.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return userInfo;
    }
}
