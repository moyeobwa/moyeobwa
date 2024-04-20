package momo.app.common.error;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import momo.app.common.error.dto.ErrorResponse;
import momo.app.common.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("code = {} message = {}", errorCode.getCode(), errorCode.getMessage());

        return ResponseEntity.status(e.getStatus())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(Exception e, HttpServletRequest request) {

        log.warn(
                "{} \n {}",
                request.getMethod(), request.getRequestURI(),
                e
        );

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .build();
    }
}
