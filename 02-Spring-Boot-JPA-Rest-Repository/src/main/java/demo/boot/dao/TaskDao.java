package demo.boot.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import demo.boot.model.Task;



@RepositoryRestResource
public interface TaskDao  extends CrudRepository<Task, Long>{

    
}

