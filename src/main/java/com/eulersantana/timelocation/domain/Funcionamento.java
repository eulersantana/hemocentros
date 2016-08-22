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
    @Column(name = "dia", nullable = false)
    private String dia;

    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private ZonedDateTime hora_inicio;

    @NotNull
    @Column(name = "hora_fim", nullable = false)
    private ZonedDateTime hora_fim;

    @ManyToOne
    @NotNull
    private Hemocentro hemocentro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public ZonedDateTime getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(ZonedDateTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public ZonedDateTime getHora_fim() {
        return hora_fim;
    }

    public void setHora_fim(ZonedDateTime hora_fim) {
        this.hora_fim = hora_fim;
    }

    public Hemocentro getHemocentro() {
        return hemocentro;
    }

    public void setHemocentro(Hemocentro hemocentro) {
        this.hemocentro = hemocentro;
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
            ", dia='" + dia + "'" +
            ", hora_inicio='" + hora_inicio + "'" +
            ", hora_fim='" + hora_fim + "'" +
            '}';
    }
}
