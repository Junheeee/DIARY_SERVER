package com.toy.diary.app.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="cstmr_bas")
@Data
@NamedQuery(name="CstmrBas.findAll", query="SELECT c FROM CstmrBas c")
public class CstmrBas implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CSTMR_SNO")
    private int cstmrSno;

    @Column(name="USE_YN")
    private String useYn;

    @Column(name="USER_ID")
    private String userId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="JOIN_DT")
    private Date joinDt;

    @Column(name="USER_PSWD")
    private String userPswd;

    @Column(name="PSWD_UPDT_DT")
    private Date pswdUpdtDt;

    @Column(name="RFRSH_TOKEN")
    private String rfrshToken;

    @Column(name="CSTMR_STATUS_CD")
    private String cstmrStatusCd;
}
