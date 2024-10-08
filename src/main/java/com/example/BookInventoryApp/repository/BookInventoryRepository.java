package com.example.BookInventoryApp.repository;

import com.example.BookInventoryApp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookInventoryRepository extends JpaRepository<Book, Long>{
}
