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
public class UploadImageResponseDTO {
    private String name;
    private String status;
    private String url;
    private String thumbUrl;
}
