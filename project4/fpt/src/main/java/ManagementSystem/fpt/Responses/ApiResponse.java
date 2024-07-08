package ManagementSystem.fpt.Responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ApiResponse<T> {

    public String message;
    public T data;
    public int code;

    public ResponseEntity<SuccessResponse<?>> ok(String message) {
        SuccessResponse<T> response = new SuccessResponse<>(message, null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<SuccessResponse<?>> ok(String message, T data) {
        SuccessResponse<T> response = new SuccessResponse<>(message, data, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ErrorResponse<?>> failure(String message) {
        ErrorResponse<T> response = new ErrorResponse<>(message, null, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public ResponseEntity<ErrorResponse<?>> failure(String message, T data) {
        ErrorResponse<T> response = new ErrorResponse<>(message, data, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public ResponseEntity<ErrorResponse<?>> failure(String message, T data, int code) {
        ErrorResponse<T> response = new ErrorResponse<>(message, data, code);
        HttpStatusCode statusCode = HttpStatus.BAD_REQUEST;
        if (code == HttpStatus.UNAUTHORIZED.value()) {
            statusCode = HttpStatus.UNAUTHORIZED;
        } else if (code == HttpStatus.FORBIDDEN.value()) {
            statusCode = HttpStatus.FORBIDDEN;
        } else if (code == HttpStatus.NOT_FOUND.value()) {
            statusCode = HttpStatus.NOT_FOUND;
        } else if (code == HttpStatus.METHOD_NOT_ALLOWED.value()) {
            statusCode = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (code == HttpStatus.NOT_ACCEPTABLE.value()) {
            statusCode = HttpStatus.NOT_ACCEPTABLE;
        } else if (code == HttpStatus.REQUEST_TIMEOUT.value()) {
            statusCode = HttpStatus.REQUEST_TIMEOUT;
        } else if (code == HttpStatus.UNPROCESSABLE_ENTITY.value()) {
            statusCode = HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (code == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (code == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            statusCode = HttpStatus.SERVICE_UNAVAILABLE;
        } else if (code == HttpStatus.BAD_GATEWAY.value()) {
            statusCode = HttpStatus.BAD_GATEWAY;
        } else {
            statusCode = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(statusCode).body(response);
    }
}


