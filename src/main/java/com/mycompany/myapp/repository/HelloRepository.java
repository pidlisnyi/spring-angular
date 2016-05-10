package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Hello;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Hello entity.
 */
@SuppressWarnings("unused")
public interface HelloRepository extends MongoRepository<Hello,String> {

}
