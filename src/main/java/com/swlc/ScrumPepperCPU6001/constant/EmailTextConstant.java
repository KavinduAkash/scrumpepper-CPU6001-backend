package com.swlc.ScrumPepperCPU6001.constant;

import org.springframework.stereotype.Component;

/**
 * @author hp
 */
@Component
public class EmailTextConstant {
    public String getCorporateInvitationTemplate(String corporateName, String userName) {
        return "<body>\n" +
                "<h1>" + corporateName + " invite you to join their corporate</h1>\n" +
                "</body>";
    }
}
