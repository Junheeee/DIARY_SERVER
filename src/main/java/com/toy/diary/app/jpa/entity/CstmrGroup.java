package com.toy.diary.app.jpa.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="cstmr_group")
@Data
@NamedQuery(name="CstmrGroup.findAll", query="SELECT c FROM CstmrGroup c")
public class CstmrGroup implements Serializable  {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CSTMR_GROUP_SNO")
    private int cstmrGroupSno;

    @Column(name="CSTMR_SNO")
    private int cstmrSno;

    @Column(name="GROUP_SNO")
    private int groupSno;

    @Column(name="GROUP_AUTH_CD")
    private String groupAuthCd;

    @Column(name="APRVL_YN")
    private String aprvlYn;

    @Column(name="APRVL_USER_ID")
    private String aprvlUserId;

    @Column(name="APRVL_DT")
    private Date aprvlDt;

    @Column(name="JOIN_YN")
    private String joinYn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="JOIN_DT")
    private Date joinDt;


}
