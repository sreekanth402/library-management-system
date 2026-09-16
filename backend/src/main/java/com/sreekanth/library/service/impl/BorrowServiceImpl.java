package com.sreekanth.library.service.impl;

import com.sreekanth.library.dto.BorrowRequest;
import com.sreekanth.library.dto.BorrowResponse;
import com.sreekanth.library.dto.DashboardStatsResponse;
import com.sreekanth.library.entity.Book;
import com.sreekanth.library.entity.BorrowRecord;
import com.sreekanth.library.entity.BorrowStatus;
import com.sreekanth.library.entity.Member;
import com.sreekanth.library.entity.MemberStatus;
import com.sreekanth.library.entity.User;
import com.sreekanth.library.exception.AlreadyReturnedException;
import com.sreekanth.library.exception.ApiException;
import com.sreekanth.library.exception.BookNotAvailableException;
import com.sreekanth.library.exception.BookNotFoundException;
import com.sreekanth.library.exception.MemberNotFoundException;
import com.sreekanth.library.mapper.BorrowMapper;
import com.sreekanth.library.repository.BookRepository;
import com.sreekanth.library.repository.BorrowRecordRepository;
import com.sreekanth.library.repository.MemberRepository;
import com.sreekanth.library.repository.UserRepository;
import com.sreekanth.library.service.BorrowService;
import com.sreekanth.library.service.FineCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {

    private static final int MAX_ACTIVE_LOANS = 5;

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final UserRepository userRepository;
    private final BorrowMapper borrowMapper;
    private final int loanPeriodDays;
    private final int finePerDay;

    public BorrowServiceImpl(
            BookRepository bookRepository,
            MemberRepository memberRepository,
            BorrowRecordRepository borrowRecordRepository,
            UserRepository userRepository,
            BorrowMapper borrowMapper,
            @Value("${library.loan-period-days}") int loanPeriodDays,
            @Value("${library.fine-per-day}") int finePerDay) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.userRepository = userRepository;
        this.borrowMapper = borrowMapper;
        this.loanPeriodDays = loanPeriodDays;
        this.finePerDay = finePerDay;
    }

    @Override
    @Transactional
    public BorrowResponse borrow(BorrowRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(request.getMemberId()));
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new ApiException(HttpStatus.CONFLICT, "Member is not active");
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BookNotFoundException(request.getBookId()));
        if (book.getAvailableQuantity() <= 0) {
            throw new BookNotAvailableException(book.getTitle());
        }

        long active = borrowRecordRepository.countByMemberIdAndStatus(member.getId(), BorrowStatus.BORROWED);
        if (active >= MAX_ACTIVE_LOANS) {
            throw new ApiException(HttpStatus.CONFLICT, "Member already has the maximum number of borrowed books");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);

        LocalDate today = LocalDate.now();
        BorrowRecord record = BorrowRecord.builder()
                .book(book)
                .member(member)
                .borrowDate(today)
                .dueDate(today.plusDays(loanPeriodDays))
                .status(BorrowStatus.BORROWED)
                .fine(BigDecimal.ZERO)
                .build();
        return borrowMapper.toResponse(borrowRecordRepository.save(record));
    }

    @Override
    @Transactional
    public BorrowResponse returnBook(Long borrowId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Borrow record not found: " + borrowId));
        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new AlreadyReturnedException(borrowId);
        }

        LocalDate today = LocalDate.now();
        record.setReturnDate(today);
        record.setStatus(BorrowStatus.RETURNED);
        record.setFine(FineCalculator.calculate(record.getDueDate(), today, finePerDay));

        Book book = record.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        return borrowMapper.toResponse(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowResponse> findByMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }
        return borrowRecordRepository.findByMemberIdOrderByBorrowDateDesc(memberId).stream()
                .map(borrowMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowResponse> findMine(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        Member member = memberRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No member profile linked to this account"));
        return findByMember(member.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BorrowResponse> findActive(Pageable pageable) {
        return borrowRecordRepository.findByStatus(BorrowStatus.BORROWED, pageable).map(borrowMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BorrowResponse> findHistory(Pageable pageable) {
        return borrowRecordRepository.findAllByOrderByBorrowDateDesc(pageable).map(borrowMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse stats() {
        return DashboardStatsResponse.builder()
                .totalBooks(bookRepository.count())
                .totalMembers(memberRepository.count())
                .activeBorrows(borrowRecordRepository.countByStatus(BorrowStatus.BORROWED))
                .totalFines(borrowRecordRepository.sumFines())
                .build();
    }
}
