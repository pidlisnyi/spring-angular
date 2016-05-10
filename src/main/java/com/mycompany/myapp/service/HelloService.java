package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Hello;
import com.mycompany.myapp.repository.HelloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Hello.
 */
@Service
public class HelloService {

    private final Logger log = LoggerFactory.getLogger(HelloService.class);
    
    @Inject
    private HelloRepository helloRepository;
    
    /**
     * Save a hello.
     * 
     * @param hello the entity to save
     * @return the persisted entity
     */
    public Hello save(Hello hello) {
        log.debug("Request to save Hello : {}", hello);
        Hello result = helloRepository.save(hello);
        return result;
    }

    /**
     *  Get all the hellos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Hello> findAll(Pageable pageable) {
        log.debug("Request to get all Hellos");
        Page<Hello> result = helloRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one hello by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Hello findOne(String id) {
        log.debug("Request to get Hello : {}", id);
        Hello hello = helloRepository.findOne(id);
        return hello;
    }

    /**
     *  Delete the  hello by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Hello : {}", id);
        helloRepository.delete(id);
    }
}
