package edu.itserulik.transfer.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class PersonDto {

    private String firstName;
    private String lastName;
}
