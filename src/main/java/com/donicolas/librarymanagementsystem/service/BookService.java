package com.donicolas.librarymanagementsystem.service;

import com.donicolas.librarymanagementsystem.exception.DuplicateIsbnException;
import com.donicolas.librarymanagementsystem.model.entity.Book;
import com.donicolas.librarymanagementsystem.repository.BookRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    @Transactional
    public Long create(Book book){
        if(bookRepository.existsByIsbn(book.getIsbn())) throw new DuplicateIsbnException("A book with this isbn ("+ book.getIsbn() +" ) already exists");
        return bookRepository.save(book).getId();
    }
}
