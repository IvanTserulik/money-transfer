package edu.itserulik.transfer.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class AccountDto {

    @EqualsAndHashCode.Include
    private String id;
    private BigDecimal balance;
    private PersonDto party;
}
