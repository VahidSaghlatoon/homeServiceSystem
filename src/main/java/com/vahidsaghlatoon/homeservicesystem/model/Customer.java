package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
public class Customer extends User {
    @Column
    private String photo;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<Order> orders = new HashSet<>();
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    private Credit credit;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id" , nullable = false)
    private Role role;


    @OneToMany( mappedBy = "customer" , cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();

    @Transient
    public String getPhotosImagePath() {
        if (getId() == null || photo == null) return "/images/default.png";
        return "/customer-photos/" + this.getId() + "/" + this.photo;
    }
}
