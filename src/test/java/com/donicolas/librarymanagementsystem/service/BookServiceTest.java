package com.donicolas.librarymanagementsystem.service;

import com.donicolas.librarymanagementsystem.exception.BookHasLoansException;
import com.donicolas.librarymanagementsystem.exception.BookNotFoundException;
import com.donicolas.librarymanagementsystem.exception.DuplicateIsbnException;
import com.donicolas.librarymanagementsystem.model.entity.Book;
import com.donicolas.librarymanagementsystem.repository.BookRepository;
import com.donicolas.librarymanagementsystem.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void create_returnsId_whenIsbnIsNew() {
        Book book = new Book("9780132350884", "Clean Code", "Robert C. Martin", 5);
        Book saved = new Book("9780132350884", "Clean Code", "Robert C. Martin", 5);
        ReflectionTestUtils.setField(saved, "id", 1L);

        when(bookRepository.existsByIsbn("9780132350884")).thenReturn(false);
        when(bookRepository.save(book)).thenReturn(saved);

        Long id = bookService.create(book);

        assertEquals(1L, id);
    }

    @Test
    void create_throwsException_andDoesNotSave_whenIsbnAlreadyExists() {
        Book book = new Book("9780132350884", "Clean Code", "Robert C. Martin", 5);
        when(bookRepository.existsByIsbn("9780132350884")).thenReturn(true);

        assertThrows(DuplicateIsbnException.class, () -> bookService.create(book));

        verify(bookRepository, never()).save(any());
    }

    @Test
    void delete_throwsException_andDoesNotDelete_whenBookHasLoans(){
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(loanRepository.existsByBookId(1L)).thenReturn(true);

        assertThrows(BookHasLoansException.class, () -> bookService.delete(1L));

        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void delete_throwsException_andDoesNotDelete_whenBookIsNotFound(){
        when(bookRepository.existsById(1L)).thenReturn(false);
        assertThrows(BookNotFoundException.class, () -> bookService.delete(1L));

        verify(loanRepository, never()).existsByBookId(any());
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void delete_deletesBook_whenBookExistsAndHasNoLoans(){
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(loanRepository.existsByBookId(1L)).thenReturn(false);

        bookService.delete(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void getByIsbn_returnsBook_whenIsbnExists(){
        Book book = new Book("9780132350884", "Clean Code", "Robert C. Martin", 5);
        when(bookRepository.findByIsbn("9780132350884")).thenReturn(Optional.of(book));

        Book result = bookService.getByIsbn("9780132350884");

        assertSame(book, result);

    }

    @Test
    void getByIsbn_throwsException_whenIsbnDoesNotExists(){
        when(bookRepository.findByIsbn("0000000000000")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getByIsbn("0000000000000"));
    }

    @Test
    void getAvailableQuantity_returnsTotalMinusActiveLoans_whenBookHasActiveLoans(){
        Book book = new Book("9780132350884", "Clean Code", "Robert C. Martin", 5);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(loanRepository.countByBookIdAndReturnDateIsNull(1L)).thenReturn(2L);

        int available = bookService.getAvailableQuantity(1L);
        assertEquals(3, available);
    }

    @Test
    void getAvailableQuantity_returnsTotal_whenThereAreNoActiveLoans(){
        Book book = new Book("9780132350884", "Clean Code", "Robert C. Martin", 5);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(loanRepository.countByBookIdAndReturnDateIsNull(1L)).thenReturn(0L);

        int available = bookService.getAvailableQuantity(1L);
        assertEquals(5, available);
    }

    @Test
    void getAvailableQuantity_throwsException_whenBookDoesNotExist(){
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getAvailableQuantity(1L));
        verify(loanRepository, never()).countByBookIdAndReturnDateIsNull(any());
    }
}