package com.creativity.datamanager.repository;

import com.creativity.datamanager.domain.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotoRepository extends MongoRepository<Photo, String> {
}
