package com.toy.diary.app.jpa.repository;

import com.toy.diary.app.jpa.entity.BoardBas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BoardBasRepository extends JpaRepository<BoardBas, Integer> {

    @Transactional
    @Modifying
    @Query("update BoardBas b set b.delYn = :delYn where b.boardSno in :boardSno")
    int updateDelYnByBoardSnoIn(@Param("boardSno")List<Integer> boardSnos, @Param("delYn") String delYn);
}
