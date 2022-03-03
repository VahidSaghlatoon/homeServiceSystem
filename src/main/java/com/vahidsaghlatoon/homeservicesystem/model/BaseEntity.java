package com.vahidsaghlatoon.homeservicesystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class BaseEntity<ID extends Number> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @JsonIgnore
    private Instant createDate = Instant.now();

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

