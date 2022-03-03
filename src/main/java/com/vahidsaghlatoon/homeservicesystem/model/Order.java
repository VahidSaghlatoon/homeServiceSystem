package com.vahidsaghlatoon.homeservicesystem.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
@SuperBuilder
@Table(name = "orders")
public class Order extends BaseEntity<Long> {
    @Column(nullable = false)
    private BigDecimal proposedPrice;
    @Column(nullable = false)
    private String description;
    @Builder.Default
    private LocalDateTime dateTimeRegister = LocalDateTime.now();
    private LocalDateTime dateTimeDone = LocalDateTime.now();
    @Column(nullable = false)
    private String address;
    @Builder.Default
    private String orderState = OrderState.WAITING_FOR_THE_OFFER.getStateMessage();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id" , nullable = false)
    private Comment comment;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "customer_id" , nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.REMOVE )
    @Builder.Default
    @ToString.Exclude
    private Set<Offer> offers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "sub_service_id" , nullable = false)
    private SubService subService ;

    public void setDateTimeDone() {
        this.dateTimeDone = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getId().equals(order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
