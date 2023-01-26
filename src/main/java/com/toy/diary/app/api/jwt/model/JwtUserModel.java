package com.toy.diary.app.api.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSTMR_SNO", unique = true)
    private Integer cstmrSno;

    @Column(name = "USER_ID", unique = true)
    private String userId;

    @Column(name = "USER_PSWD")
    private String userPswd;

    @Column(name = "CSTMR_STATUS_CD")
    private String cstmrStatusCd;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() { return userId; }

    @Override
    public String getPassword() { return userPswd; }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        if(cstmrStatusCd.equals("A"))
            return true;
        else
            return false;
//	        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // true -> 만료되지 않았음
    }

    // 계정이 사용 가능한지 확인하는 로직
    @Override
    public boolean isEnabled() {
        return true; // true -> 사용 가능
    }

}
