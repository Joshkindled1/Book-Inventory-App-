package com.example.BookInventoryApp.controller;


import com.example.BookInventoryApp.model.Book;
import com.example.BookInventoryApp.repository.BookInventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class BookInventoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private BookInventoryRepository bookInventoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_ENDPOINT = "/api/books";

    private Book book1, book2;
    private List<Book> bookList =new ArrayList<>();

    @BeforeEach
    void setUp() {
        bookInventoryRepository.deleteAll();

        book1 = Book.builder()
        .title("Escape")
                .author("John Green")
                .build();
        book2 = Book.builder()
                .title("Run")
                .author("Hank Brown")
                .build();

        bookList.add(book1);
        bookList.add(book2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllBooks() {
        // arrange - setup precondition
        // refer to setup()

        // act -  action or behaviour to test
        bookInventoryRepository.saveAll(bookList);

        // assert - verify the output
        List<Book> customers = bookInventoryRepository.findAll();
        assertFalse(customers.isEmpty());
        assertEquals(customers.size(), bookList.size());
    }

    @Test
    void createBook() throws Exception {

        //Arrange
        String requestBody = objectMapper.writeValueAsString(book1);
        //Act
        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        //Assert
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.title").value(book1.getTitle()))
                .andExpect(jsonPath("$.author").value(book1.getAuthor()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains(book1.getAuthor())));
    }



    @Test
    void getBookById() throws Exception {
        bookInventoryRepository.saveAll(bookList);
        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/{id}"), book1.getId()));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.author").value(book1.getAuthor()))
                .andExpect(jsonPath("$.title").value(book1.getTitle()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString())); //not null
    }


    @Test
    void updateBookById() throws Exception {
        bookInventoryRepository.save(book1);
        Book updateBook1 = bookInventoryRepository.findById(book1.getId()).get();

        book1.setAuthor("Updated author");
        book1.setTitle("Updated title");

        //converts Java object into a JSON string
        String requestBody = objectMapper.writeValueAsString(updateBook1);

        ResultActions resultActions = mockMvc.perform(put(API_ENDPOINT.concat("/{id}"), updateBook1.getId())
                .contentType(MediaType.APPLICATION_JSON).content(requestBody));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.author").value(updateBook1.getAuthor()))
                .andExpect(jsonPath("$.title").value(updateBook1.getTitle()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString())); //not null
    }

    @Test
    void deleteBook() throws Exception{
        // arrange - setup precondition
        bookInventoryRepository.save(book1);

        Book deleteBook1 = bookInventoryRepository.findById(book1.getId()).get();

        String expectedResponse = String.format("%s deleted successfully", deleteBook1.getTitle());

        // act -  action or behaviour to test
        ResultActions resultActions = mockMvc.perform(delete(API_ENDPOINT.concat("/{id}"), deleteBook1.getId()));


        // assert - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                // Checking that the response body matches the expected message
                .andExpect(result -> assertEquals(expectedResponse, result.getResponse().getContentAsString()));
    }

}