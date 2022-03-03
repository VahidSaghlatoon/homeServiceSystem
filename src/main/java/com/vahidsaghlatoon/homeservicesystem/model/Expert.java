package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity()
@Table(name = "experts")
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Expert extends User {
    @PositiveOrZero
    @Max(value = 5)
    @Min(value = 1)
    private Integer score;

    @ToString.Exclude
    @Lob
    private byte[] photo;

    @OneToOne(cascade = CascadeType.ALL)
    private Credit credit;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.REMOVE)
    @Builder.Default
    @ToString.Exclude
    private Set<Offer> offers = new HashSet<>();

    @ManyToMany()
    @JoinTable(
            name = "experts_sub_services",
            joinColumns = @JoinColumn(name = "expert_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_service_id")
    )
    @Builder.Default
    @ToString.Exclude
    private Set<SubService> subServices = new HashSet<>();

    @OneToMany( mappedBy = "expert" , cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expert expert = (Expert) o;
        return Objects.equals(getId(), expert.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public void addSubService(SubService subService) {
        this.subServices.add(subService);
    }

}
