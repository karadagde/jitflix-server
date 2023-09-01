package Jitflix.Jitflix.repository.mongo;

import Jitflix.Jitflix.entity.mongo.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieRepository extends MongoRepository<Movie, String> {
    List<Movie> findByTitle(String title);

}

