package edu.itserulik.transfer.common.exception;

public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Not enough money to transfer from account";
    }
}
