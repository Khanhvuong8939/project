package com.lamdevops.exception;

import com.lamdevops.backend.domain.Message;
import com.lamdevops.enums.MessageEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RestControllerAdvice
public class GlobalException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e) {
        return new ResponseEntity<>(e.getOriginalMessage(), HttpStatus.EXPECTATION_FAILED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> exception(NotFoundException e) {
        return new ResponseEntity<>(e.getOriginalMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> exceptionHandler(final MethodArgumentNotValidException ex) throws IOException {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> lstErrors =
                bindingResult.getFieldErrors()
                        .stream().map(msg -> msg.getField().toUpperCase() + " " + msg.getDefaultMessage())
                        .collect(Collectors.toList());
        Message message = new Message(MessageEnum.MODEL_CONSTRAINT_EXCEPTION, lstErrors,  bindingResult.getObjectName().toUpperCase());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
