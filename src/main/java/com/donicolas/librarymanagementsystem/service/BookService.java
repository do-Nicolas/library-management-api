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
}
