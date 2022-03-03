package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "sub_services")
public class SubService extends BaseEntity<Long> {
    @Column(nullable = false, length = 80)
    private String title;
    @Column(nullable = false)
    private String description;
    @Positive
    @Column(nullable = false)
    private BigDecimal basePrice;



    @Builder.Default
    @ToString.Exclude
    @ManyToMany(mappedBy = "subServices", cascade = CascadeType.ALL)
    private Set<Expert> listExperts = new HashSet<>();

    @OneToMany(mappedBy = "subService", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<Order> orders = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubService)) return false;
        SubService that = (SubService) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
