package edu.itserulik.transfer.model.document;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Person {

    private String firstName;
    private String lastName;
}
