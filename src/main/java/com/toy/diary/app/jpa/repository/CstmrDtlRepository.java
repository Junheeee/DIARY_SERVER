package com.toy.diary.app.jpa.repository;

import com.toy.diary.app.jpa.entity.CstmrDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CstmrDtlRepository extends JpaRepository<CstmrDtl, Integer> {
}
