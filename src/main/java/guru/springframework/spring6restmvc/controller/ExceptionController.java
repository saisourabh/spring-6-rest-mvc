package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.NotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Primary
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ArithmeticException.class)
    ResponseEntity handleArthamaticErrors(ArithmeticException exception) {
        System.out.println("ExceptionController:handleArthamaticErrors");
        return ResponseEntity.badRequest().body(exception.getLocalizedMessage().toString() + "\nSourabh in handleArthamaticErrors");
    }
}
