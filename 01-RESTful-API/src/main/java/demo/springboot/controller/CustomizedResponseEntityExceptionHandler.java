package demo.springboot.controller;


import java.util.Date;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice  //all controllers are proxied with throws advice [exception handling]
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


@ExceptionHandler(Exception.class)
public final ResponseEntity<Object>   handleAllExceptions (Exception ex, WebRequest request)
{
GenericExceptionResponse  genericExceptionResponse = new GenericExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
return new ResponseEntity(genericExceptionResponse , HttpStatus.INTERNAL_SERVER_ERROR);
}

@ExceptionHandler(CustomerNotFoundException.class)
public final ResponseEntity<Object>   handleCustomerNotFoundException (CustomerNotFoundException ex, WebRequest request)
{
GenericExceptionResponse  genericExceptionResponse = new GenericExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
return new ResponseEntity(genericExceptionResponse , HttpStatus.NOT_FOUND);
}





}
