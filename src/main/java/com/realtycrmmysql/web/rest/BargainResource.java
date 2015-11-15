package com.realtycrmmysql.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.realtycrmmysql.domain.Bargain;
import com.realtycrmmysql.repository.BargainRepository;
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
 * REST controller for managing Bargain.
 */
@RestController
@RequestMapping("/api")
public class BargainResource {

    private final Logger log = LoggerFactory.getLogger(BargainResource.class);

    @Inject
    private BargainRepository bargainRepository;

    /**
     * POST  /bargains -> Create a new bargain.
     */
    @RequestMapping(value = "/bargains",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bargain> createBargain(@Valid @RequestBody Bargain bargain) throws URISyntaxException {
        log.debug("REST request to save Bargain : {}", bargain);
        if (bargain.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new bargain cannot already have an ID").body(null);
        }
        Bargain result = bargainRepository.save(bargain);
        return ResponseEntity.created(new URI("/api/bargains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bargain", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bargains -> Updates an existing bargain.
     */
    @RequestMapping(value = "/bargains",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bargain> updateBargain(@Valid @RequestBody Bargain bargain) throws URISyntaxException {
        log.debug("REST request to update Bargain : {}", bargain);
        if (bargain.getId() == null) {
            return createBargain(bargain);
        }
        Bargain result = bargainRepository.save(bargain);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bargain", bargain.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bargains -> get all the bargains.
     */
    @RequestMapping(value = "/bargains",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Bargain>> getAllBargains(Pageable pageable)
        throws URISyntaxException {
        Page<Bargain> page = bargainRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bargains");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bargains/:id -> get the "id" bargain.
     */
    @RequestMapping(value = "/bargains/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bargain> getBargain(@PathVariable Long id) {
        log.debug("REST request to get Bargain : {}", id);
        return Optional.ofNullable(bargainRepository.findOne(id))
            .map(bargain -> new ResponseEntity<>(
                bargain,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bargains/:id -> delete the "id" bargain.
     */
    @RequestMapping(value = "/bargains/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBargain(@PathVariable Long id) {
        log.debug("REST request to delete Bargain : {}", id);
        bargainRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bargain", id.toString())).build();
    }
}
