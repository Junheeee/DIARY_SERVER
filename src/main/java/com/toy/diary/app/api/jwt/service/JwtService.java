package com.toy.diary.app.api.jwt.service;

import com.toy.diary.app.api.jwt.model.JwtProfileRsModel;
import com.toy.diary.app.api.jwt.model.JwtRqModel;
import com.toy.diary.app.api.jwt.model.JwtRsModel;
import com.toy.diary.app.api.jwt.model.JwtUserModel;
import com.toy.diary.app.jpa.entity.CstmrBas;
import com.toy.diary.app.jpa.entity.CstmrDtl;
import com.toy.diary.app.jpa.repository.CstmrBasQueryRepository;
import com.toy.diary.app.jpa.repository.CstmrBasRepository;
import com.toy.diary.app.jpa.repository.CstmrDtlRepository;
import com.toy.diary.comn.code.ErrorCode;
import com.toy.diary.comn.exception.CustomException;
import com.toy.diary.comn.utils.EncryptUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class JwtService {

    @Autowired
    private CstmrBasQueryRepository query;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private CstmrBasRepository cstmrBasRepository;

    @Autowired
    private CstmrDtlRepository cstmrDtlRepository;

    public Map<String, Object> loginProcess(JwtRqModel rq) throws Exception {
        int loginError = 1; // -100 : 아이디/비밀번호가 없습니다 , -101 : 계정정보를 찾을수 없습니다 , -102 : 비밀번호가 잘못 되었습니다 , -103 : 계정을 사용할수 없습니다.

        Map<String, Object> resultMap = new HashMap<>();

        // 입력값 검증
        if(StringUtils.isEmpty(rq.getUserId()) || StringUtils.isEmpty(rq.getUserPswd())) {
            loginError = -100;
        }

        JwtUserModel userDetails = userDetailsService.loadUserByUsername(rq.getUserId());
        // 계정이 없는 경우
        if(userDetails == null) {
            loginError = -101;
        } else {
            String password = EncryptUtils.sha256Encrypt(rq.getUserPswd());
            if(!userDetails.getPassword().equals(password)) {
                loginError = -102;
            }

            if(!userDetails.isAccountNonLocked() || !userDetails.isAccountNonExpired() || !userDetails.isEnabled() || !userDetails.isCredentialsNonExpired()) {
                loginError = -103;
            }
        }

        if(loginError < 0) {
            String errorMessage = "";
            if(loginError == -100) {
                errorMessage = "Please parameter Check";
            }else if(loginError == -101) {
                errorMessage = "Account not found";

            }else if(loginError == -102) {
                errorMessage = "Password does not match";
            }else if(loginError == -103) {
                errorMessage = "Account is unavailable";
            }
            //실패 이력 저장
            //cstmrSno , String loginYn , String errorCode
            if(userDetails != null) {
//                this.historySave(userDetails.getCstmrSno(), "N", loginError+"");
            }

            resultMap.put("loginError", loginError);
            resultMap.put("errorMessage", errorMessage);

            return resultMap;
        } else {
//            //토큰 저장 처리
//            log.debug("========= refresh>>>>"  + refreshToken);
//            this.refreshTokenSave(userDetails.getCstmrSno(), refreshToken);

            JwtRsModel result = userDetailsService.tokenIssue(userDetails);

            //성공이력 저장
//            this.historySave(userDetails.getCstmrSno(), "Y", loginError+"");

            resultMap.put("loginError", loginError);
            resultMap.put("errorMessage", "");
            resultMap.put("result", result);

            return resultMap;
        }
    }

    public CstmrBas logoutProcess(int cstmrSno) throws Exception{

        Optional<CstmrBas> optional = cstmrBasRepository.findById(cstmrSno);
        if (optional.isPresent()) {
            CstmrBas entity = optional.get();
            entity.setRfrshToken("");
            return cstmrBasRepository.save(entity);
        }else {
            return null;
        }

    }

    public CstmrBas refreshTokenSave(int cstmrSno , String refreshToken) throws Exception{

        Optional<CstmrBas> optional = cstmrBasRepository.findById(cstmrSno);

        if (!optional.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOTFIND);
        }

        CstmrBas entity = optional.get();
        entity.setRfrshToken(refreshToken);

        return cstmrBasRepository.save(entity);
    }

    public Map<String, Object> kakaoLogin(String token) throws Exception {
        String host = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> resultMap = new HashMap<>();
        try{
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            log.info("responseCode (login)= " + responseCode);

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
                JwtUserModel userDetails = new JwtUserModel();
                userDetails.setUserId(checkResult.getUserId());
                userDetails.setCstmrSno(checkResult.getCstmrSno());
                userDetails.setCstmrStatusCd(checkResult.getCstmrStatusCd());

                if(checkResult.getUseYn().equals("Y")) {
                    //계정연동 잘되어 있는 경우 -> 그냥 로그인~
                    JwtRsModel result = userDetailsService.tokenIssue(userDetails);
                    resultMap.put("result", result);

                } else {
                    //등록은 해봤는데 연결해제 해서 논리삭제 된 경우
                    checkResult.setUseYn("Y");
                    checkResult.setJoinDt(timestamp);
                    cstmrBasRepository.save(checkResult);

                    JwtRsModel result = userDetailsService.tokenIssue(userDetails);
                    resultMap.put("result", result);
                }
            } else {
                //아예 첫 등록
                bas.setUserId(id);
                bas.setUseYn("Y");
                bas.setJoinDt(timestamp);
                bas.setCstmrStatusCd("A");
                dtl.setMemberNm(nickname);
                dtl.setEmail(email);
                cstmrBasRepository.save(bas);
                cstmrDtlRepository.save(dtl);

                CstmrBas getNo = query.kakaoUserCheck(id);
                JwtUserModel userDetails = new JwtUserModel();
                userDetails.setUserId(id);
                userDetails.setCstmrStatusCd("A");
                userDetails.setCstmrSno(getNo.getCstmrSno());

                JwtRsModel result = userDetailsService.tokenIssue(userDetails);
                resultMap.put("result", result);
            }

            br.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    public JwtProfileRsModel profile(int cstmrSno) {
        JwtProfileRsModel model = query.findUserProfile(cstmrSno);

        return model;
    }

    public int kakaoUnlink(String token) throws Exception {
        URL url = new URL("https://kapi.kakao.com/v1/user/unlink");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        Map<String, Object> userInfo = kakaoLogin(token);
//        String id = (String)userInfo.get("id");
        JwtRsModel userDetails = (JwtRsModel)userInfo.get("result");

        urlConnection.setRequestProperty("Authorization", "Bearer " + token);
        urlConnection.setRequestMethod("POST");

        int result = urlConnection.getResponseCode();

        if(result == 200) {
//            CstmrBas bas = cstmrBasQueryRepository.unlinkBas(id);
            CstmrBas bas = query.unlinkBas(userDetails.getUserId());
            bas.setUseYn("N");
            cstmrBasRepository.save(bas);
        }

        log.info("response code(unLink): " + result);

        return result;
    }

    public boolean register(JwtRqModel rq) {
        Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);

        CstmrBas bas = new CstmrBas();
        bas.setUserId(rq.getUserId());
        bas.setUserPswd(EncryptUtils.sha256Encrypt(rq.getUserPswd()));
        bas.setUseYn("Y");
        bas.setJoinDt(timestamp);
        bas.setCstmrStatusCd("A");

        if(cstmrBasRepository.save(bas) == null)
            throw new CustomException(ErrorCode.FAIL);

        return true;
    }
}
