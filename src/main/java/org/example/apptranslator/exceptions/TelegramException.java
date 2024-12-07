package org.example.apptranslator.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class TelegramException extends RuntimeException {

    private HttpStatus status;
    private final Long userId;


    public TelegramException(String message, HttpStatus status, Long userId) {
        super(message);
        this.status = status;
        this.userId = userId;
    }

    public static TelegramException restThrow(String message, Long userId) {
        return new TelegramException(message, HttpStatus.BAD_REQUEST, userId);
    }
}
