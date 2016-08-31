package com.eulersantana.timelocation.service;

import com.eulersantana.timelocation.domain.Endereco;
import com.eulersantana.timelocation.repository.EnderecoRepository;
import com.eulersantana.timelocation.repository.search.EnderecoSearchRepository;
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
 * Service Implementation for managing Endereco.
 */
@Service
@Transactional
public class EnderecoService {

    private final Logger log = LoggerFactory.getLogger(EnderecoService.class);
    
    @Inject
    private EnderecoRepository enderecoRepository;
    
    @Inject
    private EnderecoSearchRepository enderecoSearchRepository;
    
    /**
     * Save a endereco.
     * 
     * @param endereco the entity to save
     * @return the persisted entity
     */
    public Endereco save(Endereco endereco) {
        log.debug("Request to save Endereco : {}", endereco);
        Endereco result = enderecoRepository.save(endereco);
        enderecoSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the enderecos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Endereco> findAll(Pageable pageable) {
        log.debug("Request to get all Enderecos");
        Page<Endereco> result = enderecoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one endereco by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Endereco findOne(Long id) {
        log.debug("Request to get Endereco : {}", id);
        Endereco endereco = enderecoRepository.findOne(id);
        return endereco;
    }

    /**
     *  Delete the  endereco by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Endereco : {}", id);
        enderecoRepository.delete(id);
        enderecoSearchRepository.delete(id);
    }

    /**
     * Search for the endereco corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Endereco> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Enderecos for query {}", query);
        return enderecoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
