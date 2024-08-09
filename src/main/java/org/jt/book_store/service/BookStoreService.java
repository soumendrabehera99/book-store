package org.jt.book_store.service;

import java.util.List;
import java.util.stream.Collectors;

import org.jt.book_store.dto.HomePageRequest;
import org.jt.book_store.model.BookStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookStoreService {
    private final JdbcTemplate jdbcTemplate;

    public void createBook(BookStore bookStore) {
        String sql = """
                INSERT INTO book_store(
                book_name,author_name,
                total_page,publication_date,
                book_format,category,
                availability,book_description
                )values(?,?,?,?,?,?,?,?)
                """;
        jdbcTemplate.update(sql,
                bookStore.getBookName(),
                bookStore.getAuthorName(),
                bookStore.getTotalPage(),
                bookStore.getPublicationDate(),
                bookStore.getBookFormat().name(),
                bookStore.getCategory(),
                listToCSV(bookStore.getAvailability()),
                bookStore.getDescription());
    }

    private String listToCSV(List<String> list) {
        return list.stream().collect(Collectors.joining(","));
    }

    public List<HomePageRequest> getBooks() {
        String sql = "SELECT book_id,book_name from book_store";
        return jdbcTemplate.query(sql, getBooksRowMapper());

    }

    private RowMapper<HomePageRequest> getBooksRowMapper() {
        RowMapper<HomePageRequest> rowMapper = (resultSet, rowNumber) -> {
            int bookId = resultSet.getInt("book_id");
            String bookName = resultSet.getString("book_Name");

            return new HomePageRequest(bookId, bookName);
        };
        return rowMapper;
    }
}
