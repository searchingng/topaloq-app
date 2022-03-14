package com.company.topaloq.exceptions.handler;

import com.company.topaloq.exceptions.BadRequestException;
import com.company.topaloq.exceptions.ForbiddenException;
import com.company.topaloq.exceptions.ItemNotFoundException;
import com.company.topaloq.exceptions.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({ItemNotFoundException.class, BadRequestException.class})
    public ResponseEntity handleException(RuntimeException ex){
        ex.printStackTrace();
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity handleException(ForbiddenException ex){
        ex.printStackTrace();
        return ResponseEntity.status(403).body(ex.getMessage());
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity handleException(UnauthorizedException ex){
        ex.printStackTrace();
        return ResponseEntity.status(401).body(ex.getMessage());
    }
}
