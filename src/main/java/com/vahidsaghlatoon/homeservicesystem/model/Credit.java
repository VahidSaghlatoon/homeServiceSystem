package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Table(name = "credits")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@SuperBuilder
public class Credit  extends BaseEntity<Long>{

    @Column(name = "credit_amount")
    @PositiveOrZero
    private BigDecimal creditAmount;

    @OneToOne(mappedBy = "credit")
    private Expert expert;

    @OneToOne(mappedBy = "credit")
    private Customer customer;

}
