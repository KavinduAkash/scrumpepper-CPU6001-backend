package com.swlc.ScrumPepperCPU6001.util;

import com.swlc.ScrumPepperCPU6001.constant.EmailTextConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * @author hp
 */
@Component
@Log4j2
public class EmailSender {
    private final Environment environment;
    private final EmailTextConstant emailTextConstant;
    @Value("${spring.mail.from}")
    private String mailFrom;
    @Value("${spring.mail.password}")
    private String password;
    @Autowired
    private JavaMailSender javaMailSender;

    public EmailSender(Environment environment, EmailTextConstant emailTextConstant) {
        this.environment = environment;
        this.emailTextConstant = emailTextConstant;
    }

    public void sendCorporateInvitations(String recipient, String subject, String content) throws MessagingException {
        log.info("Start function sendCorporateInvitations : recipient : " + recipient);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            message.setFrom(mailFrom);

            log.info("recipient : " + recipient);
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            message.setSubject(subject);

            // This mail has 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html; charset=utf-8");
            // add it
            multipart.addBodyPart(messageBodyPart);

            // put everything together
            message.setContent(multipart);

            sendHtmlEmail(message);
            log.info("Corporate employee invitation email successfully dispatched.");

        } catch (MessagingException e) {
            log.error("Function : sendCorporateInvitations " + e.getMessage(), e);
            throw e;
        }
    }

    public void sendHtmlEmail(MimeMessage message) {
        try {
            log.info("Start function sendHtmlEmail");

            javaMailSender.send(message);

            log.info("E-mail Sent.!");
        } catch (Exception e) {
            log.error("Failed to send HTML email: " + e.getMessage(), e);
            throw e;
        }
    }

    public void sendCorporateInvitations2(String recipient, String subject, String content) {
        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailFrom,password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipient));
            message.setSubject(subject);
            message.setContent(content,"text/html");

            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) {

            System.out.println(e);

            throw new RuntimeException(e);
        }

    }
}
