package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Person;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Person entity.
 */
@SuppressWarnings("unused")
public interface PersonRepository extends MongoRepository<Person,String> {

}
