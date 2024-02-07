package com.example.controllers;

import jakarta.validation.Valid;
import com.example.models.Book;
import com.example.services.BooksService;
import com.example.services.PeopleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, Integer page, Integer books_per_page, boolean sort_by_year) {

        if (page != null && books_per_page != null)
            model.addAttribute("books", booksService.findAll(page, books_per_page, sort_by_year));
        else
            model.addAttribute("books", booksService.findAll(sort_by_year));

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.show(id));
        model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.show(id));

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);

        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);

        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assignBookToReader(@RequestParam("person_id") int personID, @PathVariable("id") int id) {
        booksService.assignBook(id, personID);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id) {
        booksService.freeBook(id);
        return "redirect:/books/{id}";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("book") Book book) {
        return "books/search";
    }

    @PatchMapping("/search")
    public String find(@RequestParam("title") String title, RedirectAttributes redirectAttributes) {

        if (title == "")
            return "redirect:/books/search";

        List<Book> found = booksService.search(title);
        redirectAttributes.addFlashAttribute("found", found);
        redirectAttributes.addFlashAttribute("searched", "true");

        return "redirect:/books/search";
    }
}
