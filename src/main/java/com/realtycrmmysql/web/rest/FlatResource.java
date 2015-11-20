package com.realtycrmmysql.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.realtycrmmysql.domain.Flat;
import com.realtycrmmysql.repository.FlatRepository;
import com.realtycrmmysql.web.rest.util.HeaderUtil;
import com.realtycrmmysql.web.rest.util.PaginationUtil;
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

/**
 * REST controller for managing Flat.
 */
@RestController
@RequestMapping("/api")
public class FlatResource {

    private final Logger log = LoggerFactory.getLogger(FlatResource.class);

    @Inject
    private FlatRepository flatRepository;

    /**
     * POST  /flats -> Create a new flat.
     */
    @RequestMapping(value = "/flats",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Flat> createFlat(@Valid @RequestBody Flat flat) throws URISyntaxException {
        log.debug("REST request to save Flat : {}", flat);
        if (flat.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new flat cannot already have an ID").body(null);
        }
        Flat result = flatRepository.save(flat);
        return ResponseEntity.created(new URI("/api/flats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("flat", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flats -> Updates an existing flat.
     */
    @RequestMapping(value = "/flats",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Flat> updateFlat(@Valid @RequestBody Flat flat) throws URISyntaxException {
        log.debug("REST request to update Flat : {}", flat);
        if (flat.getId() == null) {
            return createFlat(flat);
        }
        Flat result = flatRepository.save(flat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("flat", flat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flats -> get all the flats.
     */
    @RequestMapping(value = "/flats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Flat>> getAllFlats(Pageable pageable)
        throws URISyntaxException {
        Page<Flat> page = flatRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/flats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /flats/:id -> get the "id" flat.
     */
    @RequestMapping(value = "/flats/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Flat> getFlat(@PathVariable Long id) {
        log.debug("REST request to get Flat : {}", id);
        return Optional.ofNullable(flatRepository.findOne(id))
            .map(flat -> new ResponseEntity<>(
                flat,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /flats/:id -> delete the "id" flat.
     */
    @RequestMapping(value = "/flats/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFlat(@PathVariable Long id) {
        log.debug("REST request to delete Flat : {}", id);
        flatRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("flat", id.toString())).build();
    }
}
