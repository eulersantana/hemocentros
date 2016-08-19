package com.eulersantana.timelocation.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eulersantana.timelocation.domain.Funcionamento;
import com.eulersantana.timelocation.service.FuncionamentoService;
import com.eulersantana.timelocation.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Funcionamento.
 */
@RestController
@RequestMapping("/api")
public class FuncionamentoResource {

    private final Logger log = LoggerFactory.getLogger(FuncionamentoResource.class);
        
    @Inject
    private FuncionamentoService funcionamentoService;
    
    /**
     * POST  /funcionamentos : Create a new funcionamento.
     *
     * @param funcionamento the funcionamento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new funcionamento, or with status 400 (Bad Request) if the funcionamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/funcionamentos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funcionamento> createFuncionamento(@Valid @RequestBody Funcionamento funcionamento) throws URISyntaxException {
        log.debug("REST request to save Funcionamento : {}", funcionamento);
        if (funcionamento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("funcionamento", "idexists", "A new funcionamento cannot already have an ID")).body(null);
        }
        Funcionamento result = funcionamentoService.save(funcionamento);
        return ResponseEntity.created(new URI("/api/funcionamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("funcionamento", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /funcionamentos : Updates an existing funcionamento.
     *
     * @param funcionamento the funcionamento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated funcionamento,
     * or with status 400 (Bad Request) if the funcionamento is not valid,
     * or with status 500 (Internal Server Error) if the funcionamento couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/funcionamentos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funcionamento> updateFuncionamento(@Valid @RequestBody Funcionamento funcionamento) throws URISyntaxException {
        log.debug("REST request to update Funcionamento : {}", funcionamento);
        if (funcionamento.getId() == null) {
            return createFuncionamento(funcionamento);
        }
        Funcionamento result = funcionamentoService.save(funcionamento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("funcionamento", funcionamento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /funcionamentos : get all the funcionamentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of funcionamentos in body
     */
    @RequestMapping(value = "/funcionamentos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Funcionamento> getAllFuncionamentos() {
        log.debug("REST request to get all Funcionamentos");
        return funcionamentoService.findAll();
    }

    /**
     * GET  /funcionamentos/:id : get the "id" funcionamento.
     *
     * @param id the id of the funcionamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the funcionamento, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/funcionamentos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funcionamento> getFuncionamento(@PathVariable Long id) {
        log.debug("REST request to get Funcionamento : {}", id);
        Funcionamento funcionamento = funcionamentoService.findOne(id);
        return Optional.ofNullable(funcionamento)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /funcionamentos/:id : delete the "id" funcionamento.
     *
     * @param id the id of the funcionamento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/funcionamentos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFuncionamento(@PathVariable Long id) {
        log.debug("REST request to delete Funcionamento : {}", id);
        funcionamentoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("funcionamento", id.toString())).build();
    }

    /**
     * SEARCH  /_search/funcionamentos?query=:query : search for the funcionamento corresponding
     * to the query.
     *
     * @param query the query of the funcionamento search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/funcionamentos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Funcionamento> searchFuncionamentos(@RequestParam String query) {
        log.debug("REST request to search Funcionamentos for query {}", query);
        return funcionamentoService.search(query);
    }


}
