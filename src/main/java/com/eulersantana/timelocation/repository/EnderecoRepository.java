package com.eulersantana.timelocation.repository;

import com.eulersantana.timelocation.domain.Endereco;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Endereco entity.
 */
@SuppressWarnings("unused")
public interface EnderecoRepository extends JpaRepository<Endereco,Long> {

}
