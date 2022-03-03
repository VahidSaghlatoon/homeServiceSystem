package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "offers")
@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Offer extends BaseEntity<Long> {

    @Column(nullable = false)
    private BigDecimal proposedPrice;
    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();
    @Column(nullable = false)
    private Integer durationOfWorkInHours;
    private Time startTime;
    @Builder.Default
    private OfferState offerState = OfferState.WAITING_FOR_THE_SELECTION;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne()
    @ToString.Exclude
    @JoinColumn(name = "expert_id", nullable = false)
    private Expert expert;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Offer)) return false;
        Offer offer = (Offer) o;
        return Objects.equals(getId(), offer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
