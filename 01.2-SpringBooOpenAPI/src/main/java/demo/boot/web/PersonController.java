package demo.boot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.boot.dao.PersonRepository;
import demo.boot.model.Person;

@RestController
public class PersonController {

	@Autowired
	PersonRepository  personRepo;
	
	@GetMapping("/person")
    public Page<Person> findPerson(Pageable pageable)
    {
    	return personRepo.findAll(pageable);
    }
}
