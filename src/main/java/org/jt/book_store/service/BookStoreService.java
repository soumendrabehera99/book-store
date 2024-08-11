package org.jt.book_store.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jt.book_store.dto.BookDetailsReponse;
import org.jt.book_store.dto.HomePageRequest;
import org.jt.book_store.model.BookFormat;
import org.jt.book_store.model.BookStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookStoreService {
    private final JdbcTemplate jdbcTemplate;

    public void createBook(BookStore bookStore, String filepath) {
        String sql = """
                INSERT INTO book_store(
                book_name,author_name,
                total_page,publication_date,
                book_format,category,
                availability,book_description,
                book_image
                )values(?,?,?,?,?,?,?,?,?)
                """;
        jdbcTemplate.update(sql,
                bookStore.getBookName(),
                bookStore.getAuthorName(),
                bookStore.getTotalPage(),
                bookStore.getPublicationDate(),
                bookStore.getBookFormat().name(),
                bookStore.getCategory(),
                listToCSV(bookStore.getAvailability()),
                bookStore.getDescription(), filepath);
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

    public BookDetailsReponse getBookDetails(int id) {
        String sql = "SELECT * FROM book_store where book_id=?";

        return jdbcTemplate.queryForObject(sql, bookDetailsRowMapper(), id);
    }

    public RowMapper<BookDetailsReponse> bookDetailsRowMapper() {
        // RowMapper<BookDetailsReponse> rowMapper
        return (resultSet, rowNumber) -> {
            int bookId = resultSet.getInt("book_id");
            String bookName = resultSet.getString("book_name");
            String authorName = resultSet.getString("author_name");
            int totalPage = resultSet.getInt("total_page");
            Date publicationDate = resultSet.getDate("publication_date");
            String bookFormat = resultSet.getString("book_format");
            String category = resultSet.getString("category");
            String availability = resultSet.getString("availability");
            String description = resultSet.getString("book_description");

            return new BookDetailsReponse(bookId, bookName, authorName, totalPage, sqlDateToLocalDate(publicationDate),
                    BookFormat.valueOf(bookFormat), category, csvToList(availability), description);
        };
    }

    private LocalDate sqlDateToLocalDate(Date date) {
        var formatter = new SimpleDateFormat("yyyy-MM-dd");
        return LocalDate.parse(formatter.format(date));
    }

    private List<String> csvToList(String availability) {
        return Arrays.stream(availability.split(",")).toList();
    }

    public String getBookImageById(int id) {
        String sql = "SELECT book_image FROM book_store where book_id = ?";
        RowMapper<String> rowMapper = (resultSet, rowNumber) -> resultSet.getString("book_image");
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void deleteBookbyId(int id) {
        String sql = "DELETE FROM book_store where book_id=?";
        jdbcTemplate.update(sql, id);
    }

    public void updateBook(int bookId, BookStore bookStore) {
        String sql = """
                UPDATE book_store set book_name =?,author_name=?,
                total_page=?,publication_date=?,
                book_format=?,category=?,
                availability=?,book_description=?
                WHERE book_id = ?
                """;

        jdbcTemplate.update(sql,
                bookStore.getBookName(),
                bookStore.getAuthorName(),
                bookStore.getTotalPage(),
                bookStore.getPublicationDate(),
                bookStore.getBookFormat().name(),
                bookStore.getCategory(),
                listToCSV(bookStore.getAvailability()),
                bookStore.getDescription(), bookId);
    }

}
