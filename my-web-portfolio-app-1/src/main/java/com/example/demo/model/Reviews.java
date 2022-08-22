package com.example.demo.model;

import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "reviews")
public class Reviews extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String userName;
    private String name;
    private String title;
    private String subject;
    private String updated;
    private String star1;
    private String star2;
    private String star3;
    private String star4;
    private String star5;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Reviews reviews = (Reviews) o;
        return id != null && Objects.equals(id, reviews.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
