package com.eulersantana.timelocation.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eulersantana.timelocation.domain.Hemocentro;
import com.eulersantana.timelocation.service.HemocentroService;
import com.eulersantana.timelocation.web.rest.util.HeaderUtil;
import com.eulersantana.timelocation.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Hemocentro.
 */
@RestController
@RequestMapping("/api")
public class HemocentroResource {

    private final Logger log = LoggerFactory.getLogger(HemocentroResource.class);
        
    @Inject
    private HemocentroService hemocentroService;
    
    /**
     * POST  /hemocentros : Create a new hemocentro.
     *
     * @param hemocentro the hemocentro to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hemocentro, or with status 400 (Bad Request) if the hemocentro has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hemocentros",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hemocentro> createHemocentro(@Valid @RequestBody Hemocentro hemocentro) throws URISyntaxException {
        log.debug("REST request to save Hemocentro : {}", hemocentro);
        if (hemocentro.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hemocentro", "idexists", "A new hemocentro cannot already have an ID")).body(null);
        }
        Hemocentro result = hemocentroService.save(hemocentro);
        return ResponseEntity.created(new URI("/api/hemocentros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hemocentro", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hemocentros : Updates an existing hemocentro.
     *
     * @param hemocentro the hemocentro to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hemocentro,
     * or with status 400 (Bad Request) if the hemocentro is not valid,
     * or with status 500 (Internal Server Error) if the hemocentro couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hemocentros",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hemocentro> updateHemocentro(@Valid @RequestBody Hemocentro hemocentro) throws URISyntaxException {
        log.debug("REST request to update Hemocentro : {}", hemocentro);
        if (hemocentro.getId() == null) {
            return createHemocentro(hemocentro);
        }
        Hemocentro result = hemocentroService.save(hemocentro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hemocentro", hemocentro.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hemocentros : get all the hemocentros.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of hemocentros in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/hemocentros",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Hemocentro>> getAllHemocentros(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Hemocentros");
        Page<Hemocentro> page = hemocentroService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hemocentros");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hemocentros/:id : get the "id" hemocentro.
     *
     * @param id the id of the hemocentro to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hemocentro, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/hemocentros/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hemocentro> getHemocentro(@PathVariable Long id) {
        log.debug("REST request to get Hemocentro : {}", id);
        Hemocentro hemocentro = hemocentroService.findOne(id);
        return Optional.ofNullable(hemocentro)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hemocentros/:id : delete the "id" hemocentro.
     *
     * @param id the id of the hemocentro to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/hemocentros/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHemocentro(@PathVariable Long id) {
        log.debug("REST request to delete Hemocentro : {}", id);
        hemocentroService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hemocentro", id.toString())).build();
    }

    /**
     * SEARCH  /_search/hemocentros?query=:query : search for the hemocentro corresponding
     * to the query.
     *
     * @param query the query of the hemocentro search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/hemocentros",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Hemocentro>> searchHemocentros(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Hemocentros for query {}", query);
        Page<Hemocentro> page = hemocentroService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/hemocentros");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
