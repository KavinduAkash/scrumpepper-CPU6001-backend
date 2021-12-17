package com.swlc.ScrumPepperCPU6001.config;

import com.swlc.ScrumPepperCPU6001.dto.AdminDTO;
import com.swlc.ScrumPepperCPU6001.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hp
 */
@Component
public class CustomTokenEnhancer extends JwtAccessTokenConverter {

    private final AdminService adminService;

    @Autowired
    public CustomTokenEnhancer(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();

        User user = (User) oAuth2Authentication.getPrincipal();

        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        AdminDTO adminDetailsByUserName = adminService.getAdminDetailsByUserName(user.getUsername());
        additionalInfo.put("user", adminDetailsByUserName);
        additionalInfo.put("user_id", adminDetailsByUserName.getId());

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        // set custom claims
        return super.enhance(oAuth2AccessToken, oAuth2Authentication);
    }

}
