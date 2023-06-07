package ru.job4j.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class.getSimpleName());
    private final ObjectMapper objectMapper;


    @ExceptionHandler(value = {NullPointerException.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", "Some of fields empty");
                put("details1", e.getMessage());
            }
        }));
        LOGGER.error(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidHandler(MethodArgumentNotValidException exc) {
        return ResponseEntity.badRequest().body(
                exc.getFieldErrors().stream()
                        .map(f -> Map.of(f.getField(),
                                String.format("%s. Actual value: %s", f.getDefaultMessage(),
                                        f.getRejectedValue())))
                        .collect(Collectors.toList()));
    }

}
