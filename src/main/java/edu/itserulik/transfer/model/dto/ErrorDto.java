package edu.itserulik.transfer.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ErrorDto {

    private String message;
}
