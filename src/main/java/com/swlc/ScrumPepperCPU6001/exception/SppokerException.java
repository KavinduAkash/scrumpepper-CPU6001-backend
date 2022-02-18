package com.swlc.ScrumPepperCPU6001.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
public class SppokerException extends RuntimeException {
    private int status;
    private String msg;

    public SppokerException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public SppokerException(int status, String msg) {
        super(msg);
        this.status = status;
        this.msg = msg;
    }
}
