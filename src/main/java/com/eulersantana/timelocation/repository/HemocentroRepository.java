package com.eulersantana.timelocation.repository;

import com.eulersantana.timelocation.domain.Hemocentro;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Hemocentro entity.
 */
@SuppressWarnings("unused")
public interface HemocentroRepository extends JpaRepository<Hemocentro,Long> {

}
