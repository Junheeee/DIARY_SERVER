package com.toy.diary.app.jpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.diary.app.api.kakao.model.KakaoModel;
import com.toy.diary.app.jpa.entity.QCstmrBas;
import com.toy.diary.app.jpa.entity.QCstrmDtl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@RequiredArgsConstructor
public class KakaoRepository {

    private final JPAQueryFactory query;

    public boolean kakaoUserCheck(String id) {
        QCstmrBas bas = QCstmrBas.cstmrBas;
        QCstrmDtl dtl = QCstrmDtl.cstrmDtl;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bas.userId.eq(id));

        boolean result = true;
        long cnt = query.select(Projections.bean(KakaoModel.class,
                        bas.userId
//                        dtl.memberNm,
//                        dtl.email
                ))
                .from(bas)
                .where(builder)
                .fetch().size();

        if(cnt > 0) result = false;

        return result;
    }


}
