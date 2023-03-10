package com.toy.diary.app.api.jwt.service;

import com.toy.diary.app.api.jwt.model.JwtRqModel;
import com.toy.diary.app.api.jwt.model.JwtRsModel;
import com.toy.diary.app.api.jwt.model.JwtUserModel;
import com.toy.diary.app.api.jwt.utils.JwtTokenUtil;
import com.toy.diary.app.jpa.repository.CstmrBasQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private CstmrBasQueryRepository query;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtService service;

    @Override
    public JwtUserModel loadUserByUsername(String username) {
        JwtUserModel model = query.findUserPassword(username);
        if(model == null) {
            return null;
        } else {
            return model;
        }
    }

    public JwtRsModel tokenIssue(JwtUserModel userDetails) throws Exception {
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        JwtRsModel result = new JwtRsModel();
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);
        result.setUserId(userDetails.getUserId());
        result.setCstmrSno(userDetails.getCstmrSno());

        service.refreshTokenSave(userDetails.getCstmrSno(), refreshToken);

        return result;
    }
}
