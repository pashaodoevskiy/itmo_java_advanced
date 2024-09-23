package itmo_java_advanced.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                return super.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults()
                        .including(ErrorAttributeOptions.Include.MESSAGE)
                );
            }
        };
    }

    @ExceptionHandler(CustomException.class) // тип класса который мы будем перехватывать
    public void handleCustomException(HttpServletResponse response, CustomException exception) throws IOException {
        response.sendError(exception.getStatus().value(), exception.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class) // тип класса который мы будем перехватывать
    public ResponseEntity<ErrorMessage> handleCustomException(MissingServletRequestParameterException exception) {
        String parameterName = exception.getParameterName();
        String message = String.format("parameter is missing: %s", parameterName);

        log.error(message);

        return ResponseEntity.status(400).body(new ErrorMessage(message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // тип класса который мы будем перехватывать
    public ResponseEntity<ErrorMessage> handleCustomException(MethodArgumentTypeMismatchException exception) {
        String parameterName = exception.getParameter().getParameterName();
        String message = String.format("wrong data for parameter: %s", parameterName);

        log.error(message);

        return ResponseEntity.status(400).body(new ErrorMessage(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // тип класса который мы будем перехватывать
    public ResponseEntity<ErrorMessage> handleCustomException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : exception.getMessage();

        return ResponseEntity.status(400).body(new ErrorMessage(message));
    }
}
