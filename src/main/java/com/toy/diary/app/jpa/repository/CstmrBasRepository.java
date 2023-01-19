package com.toy.diary.app.jpa.repository;

import com.toy.diary.app.jpa.entity.CstmrBas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CstmrBasRepository extends JpaRepository<CstmrBas, Integer> {

//    @Query("select c from CstmrBas c " +
//            "where c.cstmrSno = :cstmrSno " )
//    CstmrBas findBy
}
