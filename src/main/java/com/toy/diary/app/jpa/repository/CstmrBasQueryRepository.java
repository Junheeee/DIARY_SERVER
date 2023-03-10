package com.toy.diary.app.jpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.diary.app.api.cstmr.model.CstmrModel;
import com.toy.diary.app.api.jwt.model.JwtProfileRsModel;
import com.toy.diary.app.api.jwt.model.JwtUserModel;
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

    public CstmrBas kakaoUserCheck(String id) {
        QCstmrBas bas = QCstmrBas.cstmrBas;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bas.userId.eq(id));

        CstmrBas result = query.select(Projections.bean(CstmrBas.class,
                        bas.cstmrSno,
                        bas.userId,
                        bas.useYn,
                        bas.joinDt,
                        bas.cstmrStatusCd
                ))
                .from(bas)
                .where(builder)
                .fetchOne();

        return result;
    }

    public JwtProfileRsModel findUserProfile(int cstmrSno) {
        QCstmrBas basEntity = QCstmrBas.cstmrBas;
        QCstmrDtl dtlEntity = QCstmrDtl.cstmrDtl;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(basEntity.cstmrSno.eq(cstmrSno));


        JwtProfileRsModel model = query.select(Projections.bean(JwtProfileRsModel.class ,
                        basEntity.cstmrSno,
                        basEntity.userId,
                        dtlEntity.memberNm
                ))
                .from(basEntity)
                .leftJoin(dtlEntity)
                .on(basEntity.cstmrSno.eq(dtlEntity.cstmrSno))
                .where(builder)
                .fetchFirst();

        return model;

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
                        bas.joinDt,
                        bas.cstmrStatusCd
                        )).from(bas)
                        .where(builder)
                        .fetchOne();

        return result;
    }

    public JwtUserModel findUserPassword(String userId) {
        QCstmrBas bas = QCstmrBas.cstmrBas;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bas.userId.eq(userId));

        //입력ID값이 DB랑 일치하는지 체크
        CstmrBas entity = query.select(bas)
                .from(bas)
                .where(builder)
                .fetchFirst();

        if(entity != null) {
            JwtUserModel model = new JwtUserModel();
            model.setUserId(entity.getUserId());
            model.setCstmrSno(entity.getCstmrSno());
            model.setUserPswd(entity.getUserPswd());
            model.setCstmrStatusCd(entity.getCstmrStatusCd());

            return model;
        } else {
            return null;
        }
    }
}
