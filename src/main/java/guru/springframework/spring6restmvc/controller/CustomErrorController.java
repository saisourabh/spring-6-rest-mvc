package guru.springframework.spring6restmvc.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindErrors(MethodArgumentNotValidException exception) {
        System.out.println("handleBindErrors");
        List errors = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> map = new HashMap<>();
                    map.put(fieldError.getField(),fieldError.getDefaultMessage());
                    return map;
                }).collect(Collectors.toList());

        //return ResponseEntity.badRequest().body(exception.getBindingResult().getFieldErrors());
        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler
    ResponseEntity handleJPAViolations(TransactionSystemException exception){
        return ResponseEntity.badRequest().build();
    }


}
