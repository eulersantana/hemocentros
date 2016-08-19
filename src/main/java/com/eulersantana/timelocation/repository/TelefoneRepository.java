package com.eulersantana.timelocation.repository;

import com.eulersantana.timelocation.domain.Telefone;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Telefone entity.
 */
@SuppressWarnings("unused")
public interface TelefoneRepository extends JpaRepository<Telefone,Long> {

}
