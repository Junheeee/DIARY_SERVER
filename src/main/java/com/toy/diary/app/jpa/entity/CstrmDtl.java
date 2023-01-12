package com.toy.diary.app.jpa.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="cstmr_dtl")
@Data
@NamedQuery(name="CstmrDtl.findAll", query="SELECT c FROM CstrmDtl c")
public class CstrmDtl implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="CSTMR_SNO")
    private int cstmrSno;

    @Column(name="MEMBER_NM")
    private String memberNm;

    @Column(name="BRTHDY_DATE")
    private Date brthdyDate;

    @Column(name="EMAIL")
    private String email;

    @Column(name="HPNO")
    private String hpno;

    @Column(name="UPDATE_DT")
    private Date updateDt;

}
