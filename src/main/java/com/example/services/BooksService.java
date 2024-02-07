package com.example.services;

import com.example.models.Book;
import com.example.models.Person;
import com.example.repositories.BooksRepository;
import com.example.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("year"));

        return findAll();
    }

    public List<Book> findAll(int page, int booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();

        return findAll(page, booksPerPage);
    }

    public Book show(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = false)
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional(readOnly = false)
    public void update(int id, Book updated) {
        Book current = booksRepository.findById(id).orElse(null);

        current.setAuthor(updated.getAuthor());
        current.setYear(updated.getYear());
        current.setTitle(updated.getTitle());

        booksRepository.save(current);
    }

    @Transactional(readOnly = false)
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void assignBook(int id, int personId) {
        Book currentBook = booksRepository.findById(id).orElse(null);
        Person currentPerson = peopleRepository.findById(personId).orElse(null);

        currentBook.setOwner(currentPerson);
        currentBook.setTakenAt(new Date());

        booksRepository.save(currentBook);
    }

    @Transactional(readOnly = false)
    public void freeBook(int id) {
        Book current = booksRepository.findById(id).orElse(null);

        current.setOwner(null);
        current.setExpired(false);
        current.setTakenAt(null);

        booksRepository.save(current);
    }

    public List<Book> search(String title) {
        return booksRepository.findByTitleStartingWith(title);
    }
}
