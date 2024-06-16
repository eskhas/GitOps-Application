package org.person.myapp.Controllers;

import org.person.myapp.DTOs.SavePerson;
import org.person.myapp.DTOs.UpdatePerson;
import org.person.myapp.Models.Person;
import org.person.myapp.Repositories.PersonRepository;
import org.person.myapp.Services.AmazonS3Client;
import org.person.myapp.Services.Impl.ImageServiceImpl;
import org.person.myapp.Services.Impl.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class FormController {
    @Autowired
    private ImageServiceImpl imageService;
    @Autowired
    private PersonServiceImpl personService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    AmazonS3Client amazonS3Client;
    String bucketName = "eskova";



    @GetMapping("/person-form")
    public String createPersonForm(Model model){
        model.addAttribute("savePerson",new SavePerson());
        return "person-form";
    }
    @PostMapping(value = "/person",params ="save")
    public ModelAndView savePerson(@Validated @ModelAttribute SavePerson savePerson)
    {
        Person person = new Person();
        String path = imageService.store(savePerson.getImage());
        person.setImg_url(path);
        person.setPersonId(savePerson.getPersonId());
        person.setName(savePerson.getName());
        person.setAddress(savePerson.getAddress());
        if(personService.savePerson(person) != null){
            ModelAndView modelAndView = new ModelAndView("success");
            modelAndView.addObject("person", person);
            return modelAndView;
        }
        ModelAndView failure = new ModelAndView("failure");
        return failure;
    }

    @PutMapping(value = "/person")
    public ResponseEntity<String> updatePerson(@Validated @ModelAttribute() UpdatePerson updatePerson)
    {
        Person person = new Person();
        String base64Image = updatePerson.getImg();
        String base64ImageWithoutPrefix = base64Image.substring(base64Image.indexOf(",") + 1);
        String path = imageService.store64(base64ImageWithoutPrefix);
        person.setImg_url(path);
        person.setPersonId(updatePerson.getPersonId());
        person.setName(updatePerson.getName());
        person.setAddress(updatePerson.getAddress());
        int personId = person.getPersonId();
        personService.updatePerson(person,personId);
        return ResponseEntity.ok("success");
    }
    @DeleteMapping(value="/person",params = "delete")
    public String deletePersonById(@RequestParam("id") int personId){

        personService.deletePerson(personId);
        return "people";
    }

    @ResponseBody
    @GetMapping("/getPerson")
    public Optional<Person> fetchPerson(@RequestParam("id") int personId){
        return personRepository.findById(personId);
    }
    @GetMapping("/people")
    public String fetchPeople(Model model)
    {
        List<Person> people = personService.fetchPeople();
        for (int i = 0; i < people.size(); i++) {
            people.get(i).setImg_url(amazonS3Client.getPreSignedDownloadUrl(bucketName,people.get(i).getImg_url()));
        }
        model.addAttribute("people", people);
        model.addAttribute("person",new Person());
        return "people";
    }

    @GetMapping("/")
    public String mainPage(){
        return "index";
    }
}
