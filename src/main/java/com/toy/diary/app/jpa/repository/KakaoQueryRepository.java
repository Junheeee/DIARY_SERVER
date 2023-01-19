package com.toy.diary.app.jpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.diary.app.jpa.entity.CstmrBas;
import com.toy.diary.app.jpa.entity.QCstmrBas;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@RequiredArgsConstructor
public class KakaoQueryRepository {

    private final JPAQueryFactory query;

    public CstmrBas kakaoUserCheck(String id) {
        QCstmrBas bas = QCstmrBas.cstmrBas;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bas.userId.eq(id));

        CstmrBas result = query.select(Projections.bean(CstmrBas.class,
                        bas.cstmrSno,
                        bas.userId,
                        bas.useYn,
                        bas.joinDt
                ))
                .from(bas)
                .where(builder)
                .fetchOne();

        return result;
    }


}
