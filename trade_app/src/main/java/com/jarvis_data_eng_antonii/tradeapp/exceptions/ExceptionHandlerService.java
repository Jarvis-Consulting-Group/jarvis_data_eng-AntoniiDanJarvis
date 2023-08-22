package com.jarvis_data_eng_antonii.tradeapp.exceptions;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerService {
    @ExceptionHandler({IllegalArgumentException.class, DataRetrievalFailureException.class, TraderRemovalException.class})
    public ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}