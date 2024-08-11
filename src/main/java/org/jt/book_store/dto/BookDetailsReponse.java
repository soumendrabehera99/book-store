package org.jt.book_store.dto;

import java.time.LocalDate;
import java.util.List;

import org.jt.book_store.model.BookFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailsReponse {

        private int bookId;
        private String bookName;
        private String authorName;
        private int totalPage;
        private LocalDate publicationDate;
        private BookFormat bookFormat;
        private String category;
        private List<String> availability;
        private String description;

}
