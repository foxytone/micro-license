package org.neat0n.licensingservice.license.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;
import org.neat0n.licensingservice.license.model.generator.UUIDGenerator;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
public class License extends RepresentationModel<License> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    
    @GeneratorType(type = UUIDGenerator.class, when = GenerationTime.INSERT)
    @Column(unique = true, nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private String uuid;

    @Column(length = 120)
    private String description;
    
    @Column(nullable = false)
    private Long organizationId;
    @Column(length = 40)
    private String productName;
    @Column(length = 40)
    private String licenseType;
    @Column(length = 400)
    private String comment;
    
    public License withComment(String comment) {
        this.comment = comment;
        return this;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        License license = (License) o;
        return Objects.equals(id, license.id);
    }
    
    @Override
    public int hashCode() {
        return (int) (37 * id * getClass().hashCode());
    }
    
}