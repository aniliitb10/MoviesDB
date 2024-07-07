package learning.streamlit.moviedb.controller;

import learning.streamlit.moviedb.exception.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class InternalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<Object> handleInternalException(final InternalException ex, final WebRequest request) {
        logger.error("Internal exception [{}] from request: [{}]", ex.toString(), request.getDescription(true));
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
