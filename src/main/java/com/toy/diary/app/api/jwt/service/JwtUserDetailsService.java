package com.toy.diary.app.api.jwt.service;

import com.toy.diary.app.api.jwt.model.JwtUserModel;
import com.toy.diary.app.jpa.repository.CstmrBasQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private CstmrBasQueryRepository query;

    @Override
    public JwtUserModel loadUserByUsername(String username) {
        JwtUserModel model = query.findUserPassword(username);
        if(model == null) {
            return null;
        } else {
            return model;
        }
    }
}
