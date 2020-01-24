package edu.itserulik.transfer.service;

import edu.itserulik.transfer.model.document.Account;
import edu.itserulik.transfer.model.dto.TransferDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransferService {

    Mono<List<Account>> transferMoney(TransferDto dto);
}
