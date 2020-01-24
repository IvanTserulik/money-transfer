package edu.itserulik.transfer.controller.impl;

import com.google.inject.Inject;
import edu.itserulik.transfer.common.converter.Converter;
import edu.itserulik.transfer.controller.PostController;
import edu.itserulik.transfer.model.dto.AccountDto;
import edu.itserulik.transfer.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

import static edu.itserulik.transfer.common.converter.Converter.toDomain;

@Slf4j
public class SaveAccountController extends PostController<AccountDto, AccountDto> {

    private AccountService accountService;

    @Inject
    public SaveAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Mono<AccountDto> processRequest(Mono<AccountDto> request, Map<String, ?> parameters) {
        return request
                .flatMap(dto -> accountService.saveAccount(toDomain(dto)))
                .map(Converter::toDto);
    }

    @Override
    public String path() {
        return "/api/saveAccount";
    }

}
