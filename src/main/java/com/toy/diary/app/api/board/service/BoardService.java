package com.toy.diary.app.api.board.service;

import com.toy.diary.app.api.board.model.BoardDelModel;
import com.toy.diary.app.api.board.model.BoardRsModel;
import com.toy.diary.app.api.cstmr.model.CstmrModel;
import com.toy.diary.app.jpa.entity.BoardBas;
import com.toy.diary.app.jpa.repository.BoardBasQueryRepository;
import com.toy.diary.app.jpa.repository.BoardBasRepository;
import com.toy.diary.app.jpa.repository.CstmrBasQueryRepository;
import com.toy.diary.comn.code.ErrorCode;
import com.toy.diary.comn.exception.CustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class BoardService {

    @Autowired
    private BoardBasQueryRepository query;

    @Autowired
    private BoardBasRepository repository;

    public List<BoardRsModel> list() {
        List<BoardRsModel> model = query.list();
        return model;
    }

    public boolean add(BoardRsModel rq) {
        BoardBas bas = new BoardBas();
        bas.setTitle(rq.getTitle());
        bas.setContent(rq.getContent());
        bas.setDelYn("N");


        if(repository.save(bas) == null)
            throw new CustomException(ErrorCode.FAIL);

        return true;
    }

    public int delete(BoardDelModel rq) {
        int cnt = repository.updateDelYnByBoardSnoIn(rq.getBoardSnoList(), rq.getDelYn());
        return cnt;
    }

    public BoardRsModel detail(int boardSno) {
        BoardRsModel model = query.detail(boardSno);
        return model;
    }

    public boolean save(BoardRsModel rq) {
        BoardBas bas = new BoardBas();
        bas.setBoardSno(rq.getBoardSno());
        bas.setTitle(rq.getTitle());
        bas.setContent(rq.getContent());
        bas.setDelYn("N");

        if(repository.save(bas) == null)
            throw new CustomException(ErrorCode.FAIL);

        return true;
    }

}
