package com.eulersantana.timelocation.service;

import com.eulersantana.timelocation.domain.Telefone;
import com.eulersantana.timelocation.repository.TelefoneRepository;
import com.eulersantana.timelocation.repository.search.TelefoneSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Telefone.
 */
@Service
@Transactional
public class TelefoneService {

    private final Logger log = LoggerFactory.getLogger(TelefoneService.class);
    
    @Inject
    private TelefoneRepository telefoneRepository;
    
    @Inject
    private TelefoneSearchRepository telefoneSearchRepository;
    
    /**
     * Save a telefone.
     * 
     * @param telefone the entity to save
     * @return the persisted entity
     */
    public Telefone save(Telefone telefone) {
        log.debug("Request to save Telefone : {}", telefone);
        Telefone result = telefoneRepository.save(telefone);
        telefoneSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the telefones.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Telefone> findAll() {
        log.debug("Request to get all Telefones");
        List<Telefone> result = telefoneRepository.findAll();
        return result;
    }

    /**
     *  Get one telefone by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Telefone findOne(Long id) {
        log.debug("Request to get Telefone : {}", id);
        Telefone telefone = telefoneRepository.findOne(id);
        return telefone;
    }

    /**
     *  Delete the  telefone by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Telefone : {}", id);
        telefoneRepository.delete(id);
        telefoneSearchRepository.delete(id);
    }

    /**
     * Search for the telefone corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Telefone> search(String query) {
        log.debug("Request to search Telefones for query {}", query);
        return StreamSupport
            .stream(telefoneSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
