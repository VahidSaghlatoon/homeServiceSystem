package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.time.LocalDateTime;

@MappedSuperclass
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "users")
public class User extends BaseEntity<Long> {
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    @Column(name = "national_code", unique = true, nullable = false, length = 10)
    private String nationalCode;
    @Column(nullable = false, unique = true, length = 128)
    private String email;
    @Column(nullable = false, length = 64)
    @ToString.Exclude
    private String password;
    @Column(name = "user_state", nullable = false)
    @Builder.Default()
    private UserState userState = UserState.NEW;
    @Column(nullable = false)
    @Builder.Default()
    private LocalDateTime registerDateTime = LocalDateTime.now();
    @Column(nullable = false)
    private boolean enabled ;
    @Column( name = "verification_code")
    private String verificationCode ;

}
