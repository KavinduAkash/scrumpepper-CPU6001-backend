package com.swlc.ScrumPepperCPU6001.config;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * @author hp
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "resource_id";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
//                anonymous().disable()
                .authorizeRequests()
// ------------ /user --------------------------------------------------------------------------------------------------
                .antMatchers(HttpMethod.POST, ApplicationConstant.API_BASE_URL + "/user/register")
                .permitAll()
                .antMatchers(HttpMethod.POST, ApplicationConstant.API_BASE_URL + "/user/check/{action}")
                .permitAll()
                .antMatchers(HttpMethod.PUT, ApplicationConstant.API_BASE_URL + "/user/update")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.DELETE, ApplicationConstant.API_BASE_URL + "/user/delete")
                .access("hasAnyRole('ROLE_USER')")
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
