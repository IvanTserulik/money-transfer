package edu.itserulik.transfer.model.document;

import lombok.*;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Account {

    @EqualsAndHashCode.Include
    private ObjectId id;
    private BigDecimal balance;
    private Person party;
}
