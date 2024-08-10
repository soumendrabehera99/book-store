package org.jt.book_store.dto;

import java.time.LocalDate;
import java.util.List;

import org.jt.book_store.model.BookFormat;

public record BookDetailsReponse(int bookId,
        String bookName,
        String authorName,
        int totalPage,
        LocalDate publicationDate,
        BookFormat bookFormat,
        String category,
        List<String> availability,
        String description) {

}
