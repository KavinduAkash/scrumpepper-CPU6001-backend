package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.entity.AdminEntity;
import com.swlc.ScrumPepperCPU6001.repository.AdminRepository;
import com.swlc.ScrumPepperCPU6001.service.Oauth2UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

/**
 * @author hp
 */

@Service(value = "userService")
@Log4j2
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class Oauth2UserServiceImpl implements Oauth2UserService, UserDetailsService {
    private final AdminRepository adminRepository;

    public Oauth2UserServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("Execute loadUserByUsername: s: " + s);
        try {
            Optional<AdminEntity> byUsername = adminRepository.findByUsername(s);
            if(!byUsername.isPresent()) throw new RuntimeException();
            return new org.springframework.security.core.userdetails.User(byUsername.get().getUsername(), byUsername.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        } catch (Exception e) {
            log.error("Execute loadUserByUsername: " + e.getMessage());
            throw e;
        }
    }
}
