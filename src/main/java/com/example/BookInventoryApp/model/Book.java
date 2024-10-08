package com.example.BookInventoryApp.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Title is mandatory")
    private String title;


    @NotBlank(message = "Author is mandatory")
    private String author;
}
