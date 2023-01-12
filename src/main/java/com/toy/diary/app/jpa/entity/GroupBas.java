package com.toy.diary.app.jpa.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="group_bas")
@Data
@NamedQuery(name="GroupBas.findAll", query="SELECT c FROM GroupBas c")
public class GroupBas implements Serializable  {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="GROUP_SNO")
    private int groupSno;

    @Column(name="GROUP_NM")
    private String groupNm;

    @Column(name="USE_YN")
    private String useYn;

    @Column(name="GROUP_TYPE_CD")
    private String groupTypeCd;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATE_DT")
    private Date createDt;

    @Column(name="UPDATE_DT")
    private Date updateDt;
}
