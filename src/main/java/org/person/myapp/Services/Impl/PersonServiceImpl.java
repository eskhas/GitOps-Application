package org.person.myapp.Services.Impl;

import org.person.myapp.Models.Person;
import org.person.myapp.Repositories.PersonRepository;
import org.person.myapp.Services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Override
    public Person savePerson(Person person) {
        if(personRepository.existsById(person.getPersonId())){
            return null;
        }
        if(!(Objects.nonNull(person.getName())&& !"".equalsIgnoreCase(person.getName()))){
            return null;
        }
        if(!(Objects.nonNull(person.getAddress())&& !"".equalsIgnoreCase(person.getAddress()))){
            return null;
        }
        if(!(Objects.nonNull(person.getImg_url())&& !"".equalsIgnoreCase(person.getImg_url()))){
            return null;
        }
        return personRepository.save(person);
    }

    @Override
    public boolean personExists(int driverId){
        return personRepository.existsById(driverId);
    }

    @Override
    public List<Person> fetchPeople() {
        return (List<Person>) personRepository.findAll(Sort.by(Sort.Direction.ASC,"personId"));
    }

    @Override
    public Person updatePerson(Person person, int personId) {
        Person personDB = personRepository.findById(personId).get();

        // Update name and address only if provided and not empty
        if (Objects.nonNull(person.getName()) && !"".equalsIgnoreCase(person.getName())) {
            personDB.setName(person.getName());
        }
        if (Objects.nonNull(person.getAddress()) && !"".equalsIgnoreCase(person.getAddress())) {
            personDB.setAddress(person.getAddress());
        }
        if (Objects.nonNull(person.getImg_url()) && !"".equalsIgnoreCase(person.getImg_url())) {
            personDB.setImg_url(person.getImg_url());
        }
        return personRepository.save(personDB);
    }
    @Override
    public void deletePerson(int personId) {
        personRepository.deleteById(personId);
    }
}
