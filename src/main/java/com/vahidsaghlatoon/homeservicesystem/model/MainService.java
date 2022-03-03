package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "main_services")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public class MainService extends BaseEntity<Long> {
    @NotBlank(message = "پر کردن این فیلد اجباری است")
    @Pattern(regexp = "^[\u0600-\u06FF\uFB8A\u067E\u0686\u06AF\u200C\u200F ]+$"
            , message = "فقط از حروف فارسی استفاده نمایید")
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank(message = "پر کردن این فیلد اجباری است")
    @Pattern(regexp = "^[\u0600-\u06FF\uFB8A\u067E\u0686\u06AF\u200C\u200F ]+$"
            , message = "فقط از حروف فارسی استفاده نمایید")
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private boolean enabled;




    @OneToOne
    @JoinColumn(name = "mainService_id")
    private MainService mainService ;

    @OneToMany(mappedBy = "mainService" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<MainService> subServices = new HashSet<>();
}
