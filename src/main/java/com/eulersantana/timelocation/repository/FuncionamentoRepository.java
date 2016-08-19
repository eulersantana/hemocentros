package com.eulersantana.timelocation.repository;

import com.eulersantana.timelocation.domain.Funcionamento;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Funcionamento entity.
 */
@SuppressWarnings("unused")
public interface FuncionamentoRepository extends JpaRepository<Funcionamento,Long> {

}
