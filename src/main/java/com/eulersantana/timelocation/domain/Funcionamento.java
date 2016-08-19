package com.eulersantana.timelocation.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Funcionamento.
 */
@Entity
@Table(name = "funcionamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "funcionamento")
public class Funcionamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "data_inicio", nullable = false)
    private ZonedDateTime data_inicio;

    @NotNull
    @Column(name = "data_fim", nullable = false)
    private ZonedDateTime data_fim;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(ZonedDateTime data_inicio) {
        this.data_inicio = data_inicio;
    }

    public ZonedDateTime getData_fim() {
        return data_fim;
    }

    public void setData_fim(ZonedDateTime data_fim) {
        this.data_fim = data_fim;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Funcionamento funcionamento = (Funcionamento) o;
        if(funcionamento.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, funcionamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Funcionamento{" +
            "id=" + id +
            ", data_inicio='" + data_inicio + "'" +
            ", data_fim='" + data_fim + "'" +
            ", createdAt='" + createdAt + "'" +
            '}';
    }
}
