package com.toy.diary.app.api.jwt.service;

import com.toy.diary.app.api.jwt.model.JwtRqModel;
import com.toy.diary.app.api.jwt.model.JwtRsModel;
import com.toy.diary.app.api.jwt.model.JwtUserModel;
import com.toy.diary.app.api.jwt.utils.JwtTokenUtil;
import com.toy.diary.app.jpa.entity.CstmrBas;
import com.toy.diary.app.jpa.repository.CstmrBasRepository;
import com.toy.diary.comn.code.ErrorCode;
import com.toy.diary.comn.exception.CustomException;
import com.toy.diary.comn.utils.EncryptUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class JwtService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private CstmrBasRepository cstmrBasRepository;

    public Map<String, Object> loginProcess(JwtRqModel rq) throws Exception {
        int loginError = 1; // -100 : 아이디/비밀번호가 없습니다 , -101 : 계정정보를 찾을수 없습니다 , -102 : 비밀번호가 잘못 되었습니다 , -103 : 계정을 사용할수 없습니다.

        Map<String, Object> resultMap = new HashMap<>();

        // 입력값 검증
        if(StringUtils.isEmpty(rq.getUserId()) || StringUtils.isEmpty(rq.getUserPswd())) {
            loginError = -100;
        }

        JwtUserModel userDetails = (JwtUserModel)userDetailsService
                .loadUserByUsername(rq.getUserId());

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
            String accessToken = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            JwtRsModel result = new JwtRsModel();
            result.setAccessToken(accessToken);
            result.setRefreshToken(refreshToken);
            result.setUserId(userDetails.getUserId());
            result.setCstmrSno(userDetails.getCstmrSno());
            //토큰 저장 처리
            log.debug("========= refresh>>>>"  + refreshToken);
            this.refreshTokenSave(userDetails.getCstmrSno(), refreshToken);

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
}
