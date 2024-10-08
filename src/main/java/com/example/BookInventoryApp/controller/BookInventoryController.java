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
    public ResponseEntity<Book> getBookById(@PathVariable ("id") Long id) {
        Book book = bookInventoryService.findBookById(id).orElseThrow(ResourceNotFoundException::new);
        return new  ResponseEntity<> (book, HttpStatus.OK);
    }

    //4
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBookById(@PathVariable("id") Long bookId, @RequestBody @Valid Book book){
        Book updateBook = bookInventoryService.findBookById(bookId).map(b -> {
            b.setAuthor(book.getAuthor());
            b.setTitle(book.getTitle());
            return bookInventoryService.saveBook(b);
        }).orElseThrow(() -> new ResourceNotFoundException());
        return new ResponseEntity<>(updateBook, HttpStatus.OK);

    }

    //5
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBookById (@PathVariable("id")Long bookId){

        Book deleteBook = bookInventoryService.findBookById(bookId).map(b->{
            bookInventoryService.deleteBookById(b.getId());

            return b;
        }).orElseThrow(ResourceNotFoundException::new);

        String response = String.format("%s deleted successfully", deleteBook.getTitle());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

