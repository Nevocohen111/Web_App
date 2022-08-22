package com.example.demo.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table (name = "purchase")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Size(min = 1, max = 10,message = "Name must be between 1 and 10 characters")
    private String userName;
    @Size(min = 3, message = "Subject must be at least 3 characters long!")
    private String ownerName;
    @Pattern(regexp = "^([0-1]?\\d|2[0-3]):[0-5]\\d$", message = "Time must be in the format of hh:mm or hh:mm:ss")
    private String ownerLastName;
    @Pattern(regexp = "^(High|Medium|Low|high|medium|low|HIGH|MEDIUM|LOW)$", message = "The urgency must be either high, medium or low!")
    private String ownerEmail;
    @Pattern(regexp = "^([0-1]?\\d|2[0-3]):[0-5]\\d$", message = "Time must be in the format of hh:mm or hh:mm:ss")
    private String productName;
    @Size(min = 3, message = "description must be at least 3 characters long!")
    private String productDescription;
    @Size(min = 2, message = "Name must be at least 2 characters long!")
    private String productPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Activity activity = (Activity) o;
        return id != null && Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
