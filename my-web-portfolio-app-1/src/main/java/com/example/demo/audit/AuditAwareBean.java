package com.example.demo.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component("AuditAwareBean")
public class AuditAwareBean implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        //in order to get the updatedBy and createdBy, we need to check if the current authenticated role is admin or user and return its name.
        //finally it will attach the results and save them under the createdBy and updatedBy columns in the database.
        return Optional.of(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities().stream()
                        //get the role
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_USER")) ? SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream()
                        .filter(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_USER"))
                        .findFirst()
                        .get().getAuthority() : "Anonymous");
    }

}

