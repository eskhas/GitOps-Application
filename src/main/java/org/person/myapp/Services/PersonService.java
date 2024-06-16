package org.person.myapp.Services;

import org.person.myapp.Models.Person;

import java.util.List;

public interface PersonService {
    Person savePerson(Person person);
    boolean personExists(int personId);
    List<Person> fetchPeople();
    Person updatePerson(Person person, int personId);
    void deletePerson(int personId);
}
