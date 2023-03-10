package com.toy.diary.app.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="board_bas")
@Data
@NamedQuery(name="BoardBas.findAll", query="SELECT b from BoardBas b")
public class BoardBas implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BOARD_SNO")
    private int boardSno;

    @Column(name="TITLE")
    private String title;

    @Column(name="CONTENT")
    private String content;

    @Column(name="CREATE_DT")
    private Date createDt;

    @Column(name="CREATE_USER_ID")
    private String createUserId;

    @Column(name="DEL_YN")
    private String delYn;
}
