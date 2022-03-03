package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Role extends BaseEntity<Long> {

    @NotBlank(message = "پر کردن این فیلد اجباری است")
    @Pattern(regexp = "^[\u0600-\u06FF\uFB8A\u067E\u0686\u06AF\u200C\u200F ]+$"
            , message = "فقط از حروف فارسی استفاده نمایید")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "پر کردن این فیلد اجباری است")
    @Pattern(regexp = "^[\u0600-\u06FF\uFB8A\u067E\u0686\u06AF\u200C\u200F ]+$"
            , message = "فقط از حروف فارسی استفاده نمایید")
    @Column(nullable = false)
    private String description;

    @Builder.Default
    @ToString.Exclude
    @ManyToMany(mappedBy = "roles")
    private Set<Employee> employees = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "role" , fetch = FetchType.LAZY)
    private Set<Customer> customers = new HashSet<>();

    @Override
    public String toString() {
        return
                title;
    }
}
