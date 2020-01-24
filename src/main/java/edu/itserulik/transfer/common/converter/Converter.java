package edu.itserulik.transfer.common.converter;

import edu.itserulik.transfer.model.document.Account;
import edu.itserulik.transfer.model.document.Person;
import edu.itserulik.transfer.model.dto.AccountDto;
import edu.itserulik.transfer.model.dto.PersonDto;

public class Converter {

    public static Account toDomain(AccountDto dto) {
        return Account.builder()
                .balance(dto.getBalance())
                .party(Person.builder()
                        .firstName(dto.getParty().getFirstName())
                        .lastName(dto.getParty().getLastName())
                        .build())
                .build();
    }

    public static AccountDto toDto(Account account) {
        return AccountDto.builder()
                .id(account.getId().toHexString())
                .balance(account.getBalance())
                .party(PersonDto.builder()
                        .firstName(account.getParty().getFirstName())
                        .lastName(account.getParty().getLastName())
                        .build())
                .build();
    }
}
