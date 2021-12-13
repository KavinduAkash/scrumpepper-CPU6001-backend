package com.swlc.ScrumPepperCPU6001.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminException extends RuntimeException {
    private int status;
    private String msg;

    public AdminException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public AdminException(int status, String msg) {
        super(msg);
        this.status = status;
        this.msg = msg;
    }

}
