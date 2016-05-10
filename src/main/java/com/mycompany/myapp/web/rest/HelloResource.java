package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Hello;
import com.mycompany.myapp.service.HelloService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Hello.
 */
@RestController
@RequestMapping("/api")
public class HelloResource {

    private final Logger log = LoggerFactory.getLogger(HelloResource.class);
        
    @Inject
    private HelloService helloService;
    
    /**
     * POST  /hellos : Create a new hello.
     *
     * @param hello the hello to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hello, or with status 400 (Bad Request) if the hello has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hellos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hello> createHello(@RequestBody Hello hello) throws URISyntaxException {
        log.debug("REST request to save Hello : {}", hello);
        if (hello.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hello", "idexists", "A new hello cannot already have an ID")).body(null);
        }
        Hello result = helloService.save(hello);
        return ResponseEntity.created(new URI("/api/hellos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hello", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hellos : Updates an existing hello.
     *
     * @param hello the hello to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hello,
     * or with status 400 (Bad Request) if the hello is not valid,
     * or with status 500 (Internal Server Error) if the hello couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hellos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hello> updateHello(@RequestBody Hello hello) throws URISyntaxException {
        log.debug("REST request to update Hello : {}", hello);
        if (hello.getId() == null) {
            return createHello(hello);
        }
        Hello result = helloService.save(hello);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hello", hello.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hellos : get all the hellos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of hellos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/hellos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Hello>> getAllHellos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Hellos");
        Page<Hello> page = helloService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hellos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hellos/:id : get the "id" hello.
     *
     * @param id the id of the hello to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hello, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/hellos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hello> getHello(@PathVariable String id) {
        log.debug("REST request to get Hello : {}", id);
        Hello hello = helloService.findOne(id);
        return Optional.ofNullable(hello)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hellos/:id : delete the "id" hello.
     *
     * @param id the id of the hello to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/hellos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHello(@PathVariable String id) {
        log.debug("REST request to delete Hello : {}", id);
        helloService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hello", id.toString())).build();
    }

}
