package com.toy.diary.app.api.board.model;

import lombok.Data;

import java.util.List;

@Data
public class BoardDelModel {
    private List<Integer> boardSnoList;
    private String delYn;
}
