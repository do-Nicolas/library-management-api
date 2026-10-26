package com.donicolas.librarymanagementsystem.service;

import com.donicolas.librarymanagementsystem.exception.BookHasLoansException;
import com.donicolas.librarymanagementsystem.exception.BookNotFoundException;
import com.donicolas.librarymanagementsystem.exception.DuplicateIsbnException;
import com.donicolas.librarymanagementsystem.model.entity.Book;
import com.donicolas.librarymanagementsystem.repository.BookRepository;
import com.donicolas.librarymanagementsystem.repository.LoanRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public BookService(BookRepository bookRepository, LoanRepository loanRepository){
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }
    @Transactional
    public Long create(Book book){
        if(bookRepository.existsByIsbn(book.getIsbn())) throw new DuplicateIsbnException("A book with this isbn ("+ book.getIsbn() +" ) already exists");
        return bookRepository.save(book).getId();
    }

    @Transactional
    public void delete(Long id){
        if(!bookRepository.existsById(id)) throw new BookNotFoundException("Cannot find the book " + id + " to delete");
        if(loanRepository.existsByBookId(id)) throw new BookHasLoansException("Cannot delete book "+ id +": it has loans");
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Book getByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException("Book with ISBN " + isbn + " not found"));
    }

    @Transactional(readOnly = true)
    public int getAvailableQuantity(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Cannot find the book " + id));
        long activeLoans = loanRepository.countByBookIdAndReturnDateIsNull(id);
        return book.getTotalQntt() - (int) activeLoans;
    }
}
