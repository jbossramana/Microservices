package demo.boot.exception;


import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice  //all controllers are proxied with throws advice [exception handling]
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {



@ExceptionHandler(EmptyResultDataAccessException.class)
public ResponseEntity<String> noCityFound(DataAccessException e) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No City found");
}



}
