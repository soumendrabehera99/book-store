package org.jt.book_store.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class BookStore {
    private String bookName;
    private String bookAuthor;
    private int totalPage;
    private LocalDate publicationDate;
    private BookFormat bookFormat;
    private String category;
    private List<String> availability;
    private String description;
}
