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
public class CorporateEmployeeException extends RuntimeException {
    private int status;
    private String msg;

    public CorporateEmployeeException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CorporateEmployeeException(int status, String msg) {
        super(msg);
        this.status = status;
        this.msg = msg;
    }
}
