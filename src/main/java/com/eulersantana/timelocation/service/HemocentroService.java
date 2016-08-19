package com.eulersantana.timelocation.service;

import com.eulersantana.timelocation.domain.Hemocentro;
import com.eulersantana.timelocation.repository.HemocentroRepository;
import com.eulersantana.timelocation.repository.search.HemocentroSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Hemocentro.
 */
@Service
@Transactional
public class HemocentroService {

    private final Logger log = LoggerFactory.getLogger(HemocentroService.class);
    
    @Inject
    private HemocentroRepository hemocentroRepository;
    
    @Inject
    private HemocentroSearchRepository hemocentroSearchRepository;
    
    /**
     * Save a hemocentro.
     * 
     * @param hemocentro the entity to save
     * @return the persisted entity
     */
    public Hemocentro save(Hemocentro hemocentro) {
        log.debug("Request to save Hemocentro : {}", hemocentro);
        Hemocentro result = hemocentroRepository.save(hemocentro);
        hemocentroSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the hemocentros.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Hemocentro> findAll(Pageable pageable) {
        log.debug("Request to get all Hemocentros");
        Page<Hemocentro> result = hemocentroRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one hemocentro by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Hemocentro findOne(Long id) {
        log.debug("Request to get Hemocentro : {}", id);
        Hemocentro hemocentro = hemocentroRepository.findOne(id);
        return hemocentro;
    }

    /**
     *  Delete the  hemocentro by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Hemocentro : {}", id);
        hemocentroRepository.delete(id);
        hemocentroSearchRepository.delete(id);
    }

    /**
     * Search for the hemocentro corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Hemocentro> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Hemocentros for query {}", query);
        return hemocentroSearchRepository.search(queryStringQuery(query), pageable);
    }
}
