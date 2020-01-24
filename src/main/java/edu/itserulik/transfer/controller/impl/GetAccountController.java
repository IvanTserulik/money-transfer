package edu.itserulik.transfer.controller.impl;

import com.google.inject.Inject;
import edu.itserulik.transfer.common.converter.Converter;
import edu.itserulik.transfer.controller.GetController;
import edu.itserulik.transfer.model.dto.AccountDto;
import edu.itserulik.transfer.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GetAccountController extends GetController<Void, AccountDto> {

    private AccountService accountService;

    @Inject
    public GetAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Mono<AccountDto> processRequest(Mono<Void> request, Map<String, ?> parameters) {
        List<String> paramList = (List<String>) parameters.get("id");
        Optional<String> id = paramList.stream().findFirst();
        if (id.isEmpty()) {
            throw new NullPointerException("Cannot find id in params");
        }
        return request
                .publish(req -> Mono.from(accountService.getAccountById(id.get())))
                .map(Converter::toDto);
    }

    @Override
    public String path() {
        return "/api/account";
    }

}
