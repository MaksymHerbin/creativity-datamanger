package com.creativity.datamanager.repository;

import com.creativity.datamanager.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExampleRepository extends MongoRepository<Example, String> {
}
