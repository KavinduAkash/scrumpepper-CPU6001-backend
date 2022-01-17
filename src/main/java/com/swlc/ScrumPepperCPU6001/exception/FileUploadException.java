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
public class FileUploadException extends RuntimeException {
    private int status;
    private String msg;

    public FileUploadException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public FileUploadException(int status, String msg) {
        super(msg);
        this.status = status;
        this.msg = msg;
    }
}
