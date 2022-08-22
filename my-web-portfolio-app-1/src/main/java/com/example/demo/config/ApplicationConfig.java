package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class ApplicationConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/register", "/index", "/login","/api/**").and().authorizeRequests()
                .mvcMatchers("/index").permitAll()
                .mvcMatchers("/profile").authenticated()
                .mvcMatchers("/login").permitAll()
                .mvcMatchers("/holidays/**").permitAll()
                .mvcMatchers("/register").permitAll()
                .mvcMatchers("/inquiries/**").hasAnyRole("USER", "ADMIN")
                .mvcMatchers("/forgot-password").permitAll()
                .mvcMatchers("/logout").permitAll()
                .mvcMatchers("/search").hasAnyRole("USER", "ADMIN")
                .mvcMatchers("/contact").permitAll()
                .and().rememberMe().tokenValiditySeconds(60 * 60 * 24 * 7).key("myKey")
                .and().formLogin().loginPage("/login").failureUrl("/login?error=true")
                .and().logout().logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .permitAll().and().httpBasic();
        return http.build();
    }

}




