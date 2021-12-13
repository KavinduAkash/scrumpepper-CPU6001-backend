package com.swlc.ScrumPepperCPU6001.dto.response;

import lombok.*;

/**
 * @author hp
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorMessageResponseDTO {
    private boolean success;
    private int status;
    private String msg;
}
