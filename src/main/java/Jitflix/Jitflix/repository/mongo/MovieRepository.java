package Jitflix.Jitflix.repository.mongo;

import java.util.List;

import Jitflix.Jitflix.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
    List<Movie> findByTitle(String title);

}

