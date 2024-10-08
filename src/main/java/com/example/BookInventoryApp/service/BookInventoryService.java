package com.example.BookInventoryApp.service;

import com.example.BookInventoryApp.model.Book;
import com.example.BookInventoryApp.repository.BookInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookInventoryService{

        @Autowired
        private BookInventoryRepository bookInventoryRepository;


        public List<Book> findAllBooks() {
            return bookInventoryRepository.findAll();
        }
        public Optional<Book> findBookById(Long id) {
            return bookInventoryRepository.findById(id);
        }
        public Book saveBook(Book book) {
            return bookInventoryRepository.save(book);
        }
        public void deleteBookById(Long id) {
            bookInventoryRepository.deleteById(id);
        }

}
