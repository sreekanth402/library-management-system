package com.sreekanth.library.repository;

import com.sreekanth.library.entity.BorrowRecord;
import com.sreekanth.library.entity.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByMemberIdOrderByBorrowDateDesc(Long memberId);

    Page<BorrowRecord> findByStatus(BorrowStatus status, Pageable pageable);

    Page<BorrowRecord> findAllByOrderByBorrowDateDesc(Pageable pageable);

    long countByStatus(BorrowStatus status);

    @Query("SELECT COALESCE(SUM(b.fine), 0) FROM BorrowRecord b")
    BigDecimal sumFines();

    long countByMemberIdAndStatus(Long memberId, BorrowStatus status);
}
