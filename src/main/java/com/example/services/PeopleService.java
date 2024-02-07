package com.example.services;

import com.example.models.Book;
import com.example.models.Person;
import com.example.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person show(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = false)
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional(readOnly = false)
    public void update(int id, Person updatedPerson) {
        Person current = peopleRepository.findById(id).orElse(null);

        current.setFio(updatedPerson.getFio());
        current.setYear(updatedPerson.getYear());

        peopleRepository.save(current);
    }

    public List<Book> getBooksTakenByPersonId(int id) {
        Person person = peopleRepository.findById(id).orElse(null);

        for (Book book : person.getBooks()) {
            book.setExpired(book.isExpired());
        }

        return person.getBooks();
    }

    @Transactional(readOnly = false)
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
