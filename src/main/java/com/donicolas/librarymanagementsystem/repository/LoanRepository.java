package com.donicolas.librarymanagementsystem.repository;

import com.donicolas.librarymanagementsystem.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan,Long> {
    Optional<Loan> findByUserIdAndBookIdAndReturnDateIsNull(Long userId, Long bookId);
    long countByUserIdAndReturnDateIsNull(Long userId);
    long countByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findAllByUserIdAndReturnDateIsNull(Long userId);
    List<Loan> findAllByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findAllByBookId(Long bookId);
    List<Loan> findAllByUserId(Long userId);

}
