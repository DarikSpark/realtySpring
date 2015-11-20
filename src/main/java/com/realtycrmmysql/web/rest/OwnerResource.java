package com.realtycrmmysql.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.realtycrmmysql.domain.Owner;
import com.realtycrmmysql.repository.OwnerRepository;
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
 * REST controller for managing Owner.
 */
@RestController
@RequestMapping("/api")
public class OwnerResource {

    private final Logger log = LoggerFactory.getLogger(OwnerResource.class);

    @Inject
    private OwnerRepository ownerRepository;

    /**
     * POST  /owners -> Create a new owner.
     */
    @RequestMapping(value = "/owners",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Owner> createOwner(@Valid @RequestBody Owner owner) throws URISyntaxException {
        log.debug("REST request to save Owner : {}", owner);
        if (owner.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new owner cannot already have an ID").body(null);
        }
        Owner result = ownerRepository.save(owner);
        return ResponseEntity.created(new URI("/api/owners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("owner", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /owners -> Updates an existing owner.
     */
    @RequestMapping(value = "/owners",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Owner> updateOwner(@Valid @RequestBody Owner owner) throws URISyntaxException {
        log.debug("REST request to update Owner : {}", owner);
        if (owner.getId() == null) {
            return createOwner(owner);
        }
        Owner result = ownerRepository.save(owner);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("owner", owner.getId().toString()))
            .body(result);
    }

    /**
     * GET  /owners -> get all the owners.
     */
    @RequestMapping(value = "/owners",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Owner>> getAllOwners(Pageable pageable)
        throws URISyntaxException {
        Page<Owner> page = ownerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/owners");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /owners/:id -> get the "id" owner.
     */
    @RequestMapping(value = "/owners/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Owner> getOwner(@PathVariable Long id) {
        log.debug("REST request to get Owner : {}", id);
        return Optional.ofNullable(ownerRepository.findOne(id))
            .map(owner -> new ResponseEntity<>(
                owner,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /owners/:id -> delete the "id" owner.
     */
    @RequestMapping(value = "/owners/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        log.debug("REST request to delete Owner : {}", id);
        ownerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("owner", id.toString())).build();
    }
}
