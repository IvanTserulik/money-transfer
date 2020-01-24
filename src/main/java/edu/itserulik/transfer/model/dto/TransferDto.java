package edu.itserulik.transfer.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class TransferDto {

    private String accountFromId;
    private String accountToId;
    private BigDecimal sum;
}
