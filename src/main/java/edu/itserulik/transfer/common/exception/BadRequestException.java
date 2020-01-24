package edu.itserulik.transfer.common.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String s) {
        super(s);
    }
}
