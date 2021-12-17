package com.swlc.ScrumPepperCPU6001.constant;

/**
 * @author hp
 */
public class NativeQueryConstant {
    public static final String FILTER_ADMIN_BY_NOT_EQUAL_STATUS = "SELECT * FROM admin a WHERE a.status_type<>?1";
}
