package org.jt.book_store.controller;

import java.io.IOException;

import org.jt.book_store.dto.BookDetailsReponse;
import org.jt.book_store.model.BookStore;
import org.jt.book_store.service.BookStoreService;
import org.jt.book_store.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookStoreController {
    private final BookStoreService service;
    private final FileService fileService;

    @GetMapping({ "/home", "/" })
    public String home(Model model) {
        model.addAttribute("books", service.getBooks());
        return "home";
    }

    @GetMapping("/new-book")
    public String newBook(Model model) {
        model.addAttribute("book", new BookDetailsReponse());
        return "new-book";
    }

    @GetMapping("/details")
    public String details(@RequestParam int bookId, Model model) {
        model.addAttribute("book", service.getBookDetails(bookId));
        return "book-details";
    }

    @PostMapping("/create-update-book")
    public String addBook(@ModelAttribute BookStore bookStore,@RequestParam int bookId) throws IOException {
        if (bookId == 0) {
            String path = fileService.uploadBookImage(bookStore.getBookImage());
        service.createBook(bookStore, path);
        }else{
            service.updateBook(bookId,bookStore);
        }
        return "redirect:/home";
    }

    @GetMapping(path = "/book/image/{id}", produces = { "image/jpg", "image/jpeg", "image/png", "image/avif" })
    @ResponseBody
    public byte[] addBookImage(@PathVariable int id) throws IOException {
        String imageName = service.getBookImageById(id);
        byte[] image = fileService.getImage(imageName);

        return image;
    }

    @GetMapping("/remove-book")
    public String removeBook(@RequestParam int id) {
        service.deleteBookbyId(id);

        return "redirect:/home";
    }

    @GetMapping("/update-book")
    public String updateBook(@RequestParam int id, Model model) {
        var book = service.getBookDetails(id);
        model.addAttribute("book", book);

        return "new-book";
    }
}
