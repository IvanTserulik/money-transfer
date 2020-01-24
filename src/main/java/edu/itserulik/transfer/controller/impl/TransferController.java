package edu.itserulik.transfer.controller.impl;

import com.google.inject.Inject;
import edu.itserulik.transfer.controller.PostController;
import edu.itserulik.transfer.model.document.Account;
import edu.itserulik.transfer.model.dto.AccountDto;
import edu.itserulik.transfer.model.dto.PersonDto;
import edu.itserulik.transfer.model.dto.TransferDto;
import edu.itserulik.transfer.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class TransferController extends PostController<TransferDto, List<AccountDto>> {

    private TransferService transferService;

    @Inject
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    public Mono<List<AccountDto>> processRequest(Mono<TransferDto> request, Map<String, ?> parameters) {
        return request
                .flatMap(r -> transferService.transferMoney(r))
                .map(list -> list.stream()
                        .map(account -> AccountDto.builder()
                                .id(account.getId().toHexString())
                                .balance(account.getBalance())
                                .party(PersonDto.builder()
                                        .firstName(account.getParty().getFirstName())
                                        .lastName(account.getParty().getLastName())
                                        .build())
                                .build())
                        .collect(Collectors.toList()));
    }

    @Override
    public String path() {
        return "/api/transfer";
    }

}
