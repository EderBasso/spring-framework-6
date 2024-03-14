/*
package br.com.fantasmagorica.spring6webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(){
        return ResponseEntity.notFound().build();
    }
}

Commented out class, because on NotFoundException.java,
by using the annotation @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value Not Found")
the Exception will return a ResponseEntity


 */
