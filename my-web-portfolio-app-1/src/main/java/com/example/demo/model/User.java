package com.example.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "user")
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotBlank(message = "User name should not be empty")
    @Pattern(regexp = "^(?=.*[A-Z]).+$", message = "User name should contain at least 1 capital letter")
    private String username;
    @NotBlank(message = "Password should not be empty")
    //and a special letter
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).+$", message = "Password should contain at least 1 capital letter, 1 lowercase letter, 1 number and 1 special character")
    private String password;

    @Column(name = "reset_password_token",unique = true)
    private String resetPasswordToken;

    @Column(name = "verification_token",unique = true)
    private String verificationToken;

    @Column(name = "enabled")
    private String enabled;


    private String email;

    @Column(name = "roles")
    private String role;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(){}


    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.list(Collections.enumeration(Collections.singletonList((GrantedAuthority) () -> role)));
    }

    @Override
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override public boolean isEnabled() {
        return true;
    }

    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void setEnabled(String b) {
        this.enabled = b;
    }

    public String getEnabled() {
        return enabled;
    }
}
