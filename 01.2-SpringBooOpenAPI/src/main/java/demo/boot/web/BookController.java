package demo.boot.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import demo.boot.dao.BookRepository;
import demo.boot.exception.BookNotFoundException;
import demo.boot.model.Book;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/book")
public class BookController {
 
	
	
	
    @Autowired
    private BookRepository repository;
 
    @GetMapping("/{id}")
    public Book findById(@PathVariable long id) {
        return repository.findById(id)
            .orElseThrow(() -> new BookNotFoundException());
    }
 
    @GetMapping("/")
    public Collection<Book> findBooks() {
        return repository.getBooks();
    }
    
     
    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Book updateBook(
      @PathVariable("id") final String id, @Valid @RequestBody final Book book) {
    	repository.add(book);
        return book;
    }
    
    @GetMapping("/filter")
    public Page<Book> filterBooks(Pageable pageable) {
         return repository.getBooks(pageable);
    }
    
}