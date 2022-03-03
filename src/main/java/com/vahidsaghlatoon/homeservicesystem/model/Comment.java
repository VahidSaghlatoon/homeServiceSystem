package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class Comment extends BaseEntity<Long> {
    @Column()
    @Max(value = 5)
    @Min(value = 1)
    private Integer expertScore;
    @Column()
    private String comment;


    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name ="expert_id" , nullable = false )
    private Expert expert;


    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name ="customer_id" , nullable = false )
    private Customer customer;


}
