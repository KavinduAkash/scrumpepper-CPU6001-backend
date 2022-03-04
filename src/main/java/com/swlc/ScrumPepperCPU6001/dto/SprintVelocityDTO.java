package com.swlc.ScrumPepperCPU6001.dto;

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
public class SprintVelocityDTO {
    private SprintDTO sprint;
    private int ideal;
    private int done;

    @Override
    public String toString() {
        return "SprintVelocityDTO{" +
                "sprint=" + sprint +
                ", ideal=" + ideal +
                ", done=" + done +
                '}';
    }
}
