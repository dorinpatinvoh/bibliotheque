package com.example.bibliotheque.exception;

import com.example.bibliotheque.dto.MessageResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Resource non trouvé: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<MessageResponseDto> handleBookNotAvailableException(BookNotAvailableException ex) {
        logger.warn("Livre non disponible: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDto(ex.getMessage()));
    }
    
    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<MessageResponseDto> handleOperationNotAllowedException(OperationNotAllowedException ex) {
        logger.warn("Opération non autorisée: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class) // Ex: utilisateur déjà existant
    public ResponseEntity<MessageResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Argument illégal: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDto(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("Erreurs de validation: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponseDto> handleGlobalException(Exception ex) {
        logger.error("Erreur inattendue: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponseDto("Une erreur interne est survenue. Veuillez réessayer plus tard."));
    }
}