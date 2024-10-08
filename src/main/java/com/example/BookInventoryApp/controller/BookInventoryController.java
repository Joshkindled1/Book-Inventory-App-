package com.example.BookInventoryApp.controller;

import com.example.BookInventoryApp.exception.ResourceNotFoundException;
import com.example.BookInventoryApp.model.Book;
import com.example.BookInventoryApp.service.BookInventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookInventoryController {
    @Autowired
    private BookInventoryService bookInventoryService;


    //1
    @GetMapping
    public ResponseEntity<Object> getAllBooks() {

        List<Book> bookList = bookInventoryService.findAllBooks();

        if(((List<?>) bookList).isEmpty()) throw new ResourceNotFoundException();

        return new  ResponseEntity<> (bookInventoryService.findAllBooks(), HttpStatus.OK);
    }

    //2
    @PostMapping
    public ResponseEntity<Object> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookInventoryService.saveBook(book);
        return new ResponseEntity<>(bookInventoryService.saveBook(book), HttpStatus.CREATED);
    }

    //3
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookInventoryService.findBookById(id).orElseThrow(()->new ResourceNotFoundException());
        return new  ResponseEntity<> (book, HttpStatus.OK);
    }

    //4
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        Optional<Book> book = bookInventoryService.findBookById(id);
        if (book.isPresent()) {
            Book updatedBook = book.get();
            updatedBook.setTitle(bookDetails.getTitle());
            updatedBook.setAuthor(bookDetails.getAuthor());
            return ResponseEntity.ok(bookInventoryService.saveBook(updatedBook));
        } else {
            throw new ResourceNotFoundException();
        }
    }

    //5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Optional<Book> book = bookInventoryService.findBookById(id);
        if (book.isPresent()) {
            bookInventoryService.deleteBookById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException();
        }
    }


}

