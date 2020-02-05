package edu.itserulik.transfer.common.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Not found");
    }
}
