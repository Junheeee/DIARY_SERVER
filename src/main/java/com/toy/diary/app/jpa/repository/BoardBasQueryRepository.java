package com.toy.diary.app.jpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.diary.app.api.board.model.BoardRsModel;
import com.toy.diary.app.api.cstmr.model.CstmrModel;
import com.toy.diary.app.jpa.entity.QBoardBas;
import com.toy.diary.app.jpa.entity.QCstmrBas;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j2
@Repository
@RequiredArgsConstructor
public class BoardBasQueryRepository {
    private final JPAQueryFactory query;

    public List<BoardRsModel> apple() {
        QBoardBas bas = QBoardBas.boardBas;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bas.delYn.eq("N"));

        List<BoardRsModel> result =
                query.select(Projections.bean(BoardRsModel.class,
                        bas.boardSno,
                        bas.title,
                        bas.content
                )).from(bas)
                        .where(builder)
                        .fetch();
        return result;
    }

    public BoardRsModel detail(int boardSno) {
        QBoardBas bas = QBoardBas.boardBas;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bas.boardSno.eq(boardSno));

        BoardRsModel result =
                query.select(Projections.bean(BoardRsModel.class,
                        bas.boardSno,
                        bas.title,
                        bas.content
                )).from(bas)
                        .where(builder)
                        .fetchOne();

        return result;
    }
}
