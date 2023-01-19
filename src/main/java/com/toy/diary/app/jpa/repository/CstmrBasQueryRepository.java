package com.toy.diary.app.jpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.diary.app.api.cstmr.model.CstmrModel;
import com.toy.diary.app.jpa.entity.CstmrBas;
import com.toy.diary.app.jpa.entity.CstmrDtl;
import com.toy.diary.app.jpa.entity.QCstmrBas;
import com.toy.diary.app.jpa.entity.QCstmrDtl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@RequiredArgsConstructor
public class CstmrBasQueryRepository {

    private final JPAQueryFactory query;

    public CstmrModel login(CstmrModel rq) {
        QCstmrBas bas = QCstmrBas.cstmrBas;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bas.userId.eq(rq.getUserId()));
        builder.and(bas.userPswd.eq(rq.getUserPswd()));

        CstmrModel result =
                query.select(Projections.bean(CstmrModel.class,
                        bas.userId,
                        bas.userPswd
                        )).from(bas)
                        .where(builder)
                        .fetchOne();



        return result;
    }

    public CstmrBas unlinkBas(String id) {
        QCstmrBas bas = QCstmrBas.cstmrBas;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bas.userId.eq(id));
        builder.and(bas.useYn.eq("Y"));

        CstmrBas result =
                query.select(Projections.bean(CstmrBas.class,
                        bas.cstmrSno,
                        bas.useYn,
                        bas.userId,
                        bas.joinDt
                        )).from(bas)
                        .where(builder)
                        .fetchOne();

        return result;
    }
}
