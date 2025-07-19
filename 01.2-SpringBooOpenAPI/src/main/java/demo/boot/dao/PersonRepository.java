package demo.boot.dao;




import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import demo.boot.model.Person;


//This repository is an interface and will allow you to perform various operations 
//involving Person objects. It gets these operations by extending the PagingAndSortingRepository
//interface defined in Spring Data Commons.


@RepositoryRestResource
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

	
	
}
