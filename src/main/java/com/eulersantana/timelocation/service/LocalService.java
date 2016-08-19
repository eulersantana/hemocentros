package com.eulersantana.timelocation.service;

import com.eulersantana.timelocation.domain.Local;
import com.eulersantana.timelocation.repository.LocalRepository;
import com.eulersantana.timelocation.repository.search.LocalSearchRepository;
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
 * Service Implementation for managing Local.
 */
@Service
@Transactional
public class LocalService {

    private final Logger log = LoggerFactory.getLogger(LocalService.class);
    
    @Inject
    private LocalRepository localRepository;
    
    @Inject
    private LocalSearchRepository localSearchRepository;
    
    /**
     * Save a local.
     * 
     * @param local the entity to save
     * @return the persisted entity
     */
    public Local save(Local local) {
        log.debug("Request to save Local : {}", local);
        Local result = localRepository.save(local);
        localSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the locals.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Local> findAll(Pageable pageable) {
        log.debug("Request to get all Locals");
        Page<Local> result = localRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one local by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Local findOne(Long id) {
        log.debug("Request to get Local : {}", id);
        Local local = localRepository.findOne(id);
        return local;
    }

    /**
     *  Delete the  local by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Local : {}", id);
        localRepository.delete(id);
        localSearchRepository.delete(id);
    }

    /**
     * Search for the local corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Local> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Locals for query {}", query);
        return localSearchRepository.search(queryStringQuery(query), pageable);
    }
}
