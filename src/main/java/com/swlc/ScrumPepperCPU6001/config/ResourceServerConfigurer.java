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
// ------------ /corporate ---------------------------------------------------------------------------------------------
                .antMatchers(HttpMethod.DELETE, ApplicationConstant.API_BASE_URL + "/corporate/create")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.DELETE, ApplicationConstant.API_BASE_URL + "/corporate/update")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.DELETE, ApplicationConstant.API_BASE_URL + "/corporate/remove")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, ApplicationConstant.API_BASE_URL + "/corporate/my-corporates")
                .access("hasAnyRole('ROLE_USER')")
// ------------ /corporate-employee ---------------------------------------------------------------------------------------------
                .antMatchers(HttpMethod.GET, ApplicationConstant.API_BASE_URL + "/corporate/employee/invitations")
                .access("hasAnyRole('ROLE_USER')")
// ------------ /project -----------------------------------------------------------------------------------------------
                .antMatchers(HttpMethod.POST, ApplicationConstant.API_BASE_URL + "/project/create")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, ApplicationConstant.API_BASE_URL + "/project/my-projects")
                .access("hasAnyRole('ROLE_USER')")
// ------------ /project-member ----------------------------------------------------------------------------------------
                .antMatchers(HttpMethod.PATCH, ApplicationConstant.API_BASE_URL + "/project-member/add")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.PATCH, ApplicationConstant.API_BASE_URL + "/project-member/update")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.DELETE, ApplicationConstant.API_BASE_URL + "/project-member/remove")
                .access("hasAnyRole('ROLE_USER')")
// ------------ /user-story --------------------------------------------------------------------------------------------
                .antMatchers(HttpMethod.POST, ApplicationConstant.API_BASE_URL + "/user-story/create")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.PATCH, ApplicationConstant.API_BASE_URL + "/user-story/create")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, ApplicationConstant.API_BASE_URL + "/user-story/create/lbl")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.PATCH, ApplicationConstant.API_BASE_URL + "/user-story/move")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, ApplicationConstant.API_BASE_URL + "/user-story/get-sprint")
                .access("hasAnyRole('ROLE_USER')")
// ------------ /sprint ---------------------------------------------------------------------=--------------------------
                .antMatchers(HttpMethod.POST, ApplicationConstant.API_BASE_URL + "/sprint/add")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.PATCH, ApplicationConstant.API_BASE_URL + "/sprint/update")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, ApplicationConstant.API_BASE_URL + "/sprint/get")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.PATCH, ApplicationConstant.API_BASE_URL + "/sprint/start")
                .access("hasAnyRole('ROLE_USER')")
// ------------ /docs ---------------------------------------------------------------------=--------------------------
                .antMatchers(HttpMethod.POST, ApplicationConstant.API_BASE_URL + "/docs/create")
                .access("hasAnyRole('ROLE_USER')")
                .antMatchers(HttpMethod.PATCH, ApplicationConstant.API_BASE_URL + "/docs/update")
                .access("hasAnyRole('ROLE_USER')")
// ------------ /docs ---------------------------------------------------------------------=--------------------------
                .antMatchers(HttpMethod.GET, ApplicationConstant.API_BASE_URL + "/sppoker/room/get/{projectId}")
                .access("hasAnyRole('ROLE_USER')")
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
