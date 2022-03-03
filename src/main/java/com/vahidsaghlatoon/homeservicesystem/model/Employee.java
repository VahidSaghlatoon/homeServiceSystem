package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
public class Employee extends BaseEntity<Long> {
    @NotBlank(message = "پر کردن این فیلد اجباری است")
    @Pattern(regexp = "^[\u0600-\u06FF\uFB8A\u067E\u0686\u06AF\u200C\u200F ]+$"
            , message = "فقط از حروف فارسی استفاده نمایید")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "پر کردن این فیلد اجباری است")
    @Pattern(regexp = "^[\u0600-\u06FF\uFB8A\u067E\u0686\u06AF\u200C\u200F ]+$"
            , message = "فقط از حروف فارسی استفاده نمایید")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "پر کردن این فیلد اجباری است")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
            , message = "ایمیل را به درستی وارد نمایید ")
    @Column(nullable = false, unique = true)
    private String email;

//    @NotBlank(message = "پر کردن این فیلد اجباری است")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
//            , message = "کلمه عبور حداقل شامل 8 حرف و باید ترکیبی از اعداد و حروف انگلیسی باشد")
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean enabled;

    @Column()
    private String photo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employees_roles",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role theRole) {
        roles.add(theRole);
    }

    @Transient
    public String getPhotosImagePath() {
        if (getId() == null || photo == null) return "/images/default.png";
        return "/employee-photos/" + this.getId() + "/" + this.photo;
    }
}
