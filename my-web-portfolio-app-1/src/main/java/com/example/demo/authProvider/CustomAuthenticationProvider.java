package com.example.demo.authProvider;

import com.example.demo.services.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final SecurityUserDetailsService userDetailsService;

    @Autowired
    public CustomAuthenticationProvider(SecurityUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //getting the username and password from the authentication object defined in the success form of spring security login form.
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (name.equals("Admin") && password.equals("Admin!111")) {
            ArrayList<GrantedAuthority> admin = new ArrayList<>();
            admin.add((GrantedAuthority) () -> "ROLE_ADMIN");
            return new UsernamePasswordAuthenticationToken(name, password, admin);
            //if the user is validated, return the user with a new USER_ROLE authority name.
        } else if(userDetailsService.isValidUser(name, password) && userDetailsService.isUserEnabled(name)) {
            ArrayList<GrantedAuthority> user = new ArrayList<>();
            user.add((GrantedAuthority) () -> "ROLE_USER");
            return new UsernamePasswordAuthenticationToken(name, password, user);
        } else {
             return null;
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}