package com.swlc.ScrumPepperCPU6001.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * @author hp
 */
@EnableAsync
@Configuration
public class BeanConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setReadTimeout(600000);
        requestFactory.setConnectTimeout(600000);
        return new RestTemplate(requestFactory);
    }
}
