package org.neat0n.licensingservice.license.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class License extends RepresentationModel<License>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    
    @Column(nullable = false)
    @Size(min = 36, max=36)
    private String licenseId;
    
    private String description;
    
    @Column(nullable = false)
    private String organizationId;
    
    private String productName;
    
    private String licenseType;
    
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
        return (int) (37*id*getClass().hashCode());
    }
}