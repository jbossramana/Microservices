package demo.boot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import demo.boot.model.Task;



@RepositoryRestResource
public interface TaskDao  extends CrudRepository<Task, Long>{

	 // 1. Find tasks by exact description
    List<Task> findByDescription(String description);

    // 2. Find tasks where description contains a keyword (case-insensitive)
    List<Task> findByDescriptionContainingIgnoreCase(String keyword);

    // 3. Custom JPQL query to get tasks where description starts with a prefix
    @Query("SELECT t FROM Task t WHERE t.description LIKE :prefix%")
    List<Task> findTasksWithPrefix(@Param("prefix") String prefix);

    // 4. Custom JPQL to find tasks by description length
    @Query("SELECT t FROM Task t WHERE LENGTH(t.description) > :minLength")
    List<Task> findTasksWithDescriptionLongerThan(@Param("minLength") int minLength);
}

