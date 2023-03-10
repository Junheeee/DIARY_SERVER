package com.toy.diary.app.api.kakao.service;

import com.toy.diary.app.api.jwt.model.JwtRsModel;
import com.toy.diary.app.api.jwt.model.JwtUserModel;
import com.toy.diary.app.api.jwt.service.JwtUserDetailsService;
import com.toy.diary.app.api.jwt.utils.JwtTokenUtil;
import com.toy.diary.app.jpa.entity.CstmrBas;
import com.toy.diary.app.jpa.entity.CstmrDtl;
import com.toy.diary.app.jpa.repository.CstmrBasQueryRepository;
import com.toy.diary.app.jpa.repository.CstmrBasRepository;
import com.toy.diary.app.jpa.repository.CstmrDtlRepository;
import com.toy.diary.app.jpa.repository.KakaoQueryRepository;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class KakaoService {

    @Autowired
    private KakaoQueryRepository query;

    @Autowired
    private CstmrBasRepository cstmrBasRepository;

    @Autowired
    private CstmrDtlRepository cstmrDtlRepository;

    @Autowired
    CstmrBasQueryRepository cstmrBasQueryRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    public Map<String, Object> login(String token) throws Exception {
        String host = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> resultMap = new HashMap<>();
        try{
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            log.info("responseCode = " + responseCode);

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
            String email = kakao_account.get("email").toString();

//            userInfo.put("id", id);
//            userInfo.put("nickname", nickname);

            CstmrBas bas = new CstmrBas();
            CstmrDtl dtl = new CstmrDtl();

            Long datetime = System.currentTimeMillis();
            Timestamp timestamp = new Timestamp(datetime);

            CstmrBas checkResult = query.kakaoUserCheck(id);
            if(checkResult != null) {
                if(checkResult.getUseYn().equals("Y")) {
                    //계정연동 잘되어 있는 경우 -> 그냥 로그인~
                    JwtUserModel userDetails = new JwtUserModel();
                    userDetails.setUserId(checkResult.getUserId());
                    userDetails.setCstmrSno(checkResult.getCstmrSno());
                    userDetails.setCstmrStatusCd(checkResult.getCstmrStatusCd());

                    JwtRsModel result = userDetailsService.tokenIssue(userDetails);
                    resultMap.put("result", result);

                } else {
                    //등록은 해봤는데 연결해제 해서 논리삭제 된 경우
                    checkResult.setUseYn("Y");
                    checkResult.setJoinDt(timestamp);
                    cstmrBasRepository.save(checkResult);
                }
            } else {
                //아예 첫 등록
                bas.setUserId(id);
                bas.setUseYn("Y");
                bas.setJoinDt(timestamp);
                dtl.setMemberNm(nickname);
                cstmrBasRepository.save(bas);
                cstmrDtlRepository.save(dtl);
            }

            br.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    public int unlink(String token) throws Exception {
        URL url = new URL("https://kapi.kakao.com/v1/user/unlink");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        Map<String, Object> userInfo = this.login(token);
//        String id = (String)userInfo.get("id");
        JwtRsModel userDetails = (JwtRsModel)userInfo.get("result");

        urlConnection.setRequestProperty("Authorization", "Bearer " + token);
        urlConnection.setRequestMethod("POST");

        int result = urlConnection.getResponseCode();

        if(result == 200) {
//            CstmrBas bas = cstmrBasQueryRepository.unlinkBas(id);
            CstmrBas bas = cstmrBasQueryRepository.unlinkBas(userDetails.getUserId());
            bas.setUseYn("N");
            cstmrBasRepository.save(bas);
        }

        log.info("response code: " + result);

        return result;
    }


}
