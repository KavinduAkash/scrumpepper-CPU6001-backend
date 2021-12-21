package com.swlc.ScrumPepperCPU6001.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCorporateRequestDTO {
    private long id;

    @Override
    public String toString() {
        return "DeleteCorporateRequestDTO{" +
                "id=" + id +
                '}';
    }
}
