package com.example.demo.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "contact")
public class Contact extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must contain at least 3 characters")
    private String name;
    @Size(min = 3, max = 50, message = "Name must contain at least 3 characters")
    private String userName;
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be 10 digits")
    private String mobile_num;
    @Email(message = "Email is not valid")
    private String email;
    @NotBlank(message = "Subject is required")
    private String subject;
    @NotBlank(message = "Message is required")
    private String message;
    private String state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Contact contact = (Contact) o;
        return id != null && Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
