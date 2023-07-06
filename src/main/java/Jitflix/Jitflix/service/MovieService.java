package Jitflix.Jitflix.service;

import Jitflix.Jitflix.entity.Movie;
import Jitflix.Jitflix.repository.mongo.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    public Movie getMovieById(String id){
        return movieRepository.findById(id).orElse(null);
    }

    public Page<Movie> getAllMovies(){
        return movieRepository.findAll(Pageable.ofSize(20));
    }

    public Movie saveMovie(Movie movie){
        return movieRepository.save(movie);
    }

    public void deleteMovie(String id){
        movieRepository.deleteById(id);
    }

    public List<Movie> getMovieByTitle(String title){
        return movieRepository.findByTitle(title);
    }
    public Movie updateMovie(Movie movie){
        Movie existingMovie = movieRepository.findById(movie.getId()).orElse(null);

        if (existingMovie == null){
            return null;
        }

        existingMovie.setBucketUrl(movie.getBucketUrl());
        existingMovie.setPlot(movie.getPlot());
        existingMovie.setPoster(movie.getPoster());
        existingMovie.setGenres(movie.getGenres());
        existingMovie.setRuntime(movie.getRuntime());
        existingMovie.setCast(movie.getCast());
        existingMovie.setNum_mflix_comments(movie.getNum_mflix_comments());
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setFullplot(movie.getFullplot());
        existingMovie.setCountries(movie.getCountries());
        existingMovie.setReleased(movie.getReleased());
        existingMovie.setDirectors(movie.getDirectors());
        existingMovie.setWriters(movie.getWriters());
        existingMovie.setRated(movie.getRated());
//        existingMovie.setAwards(movie.getAwards());
        existingMovie.setLastupdated(movie.getLastupdated());
        existingMovie.setYear(movie.getYear());
//        existingMovie.setImdb(movie.getImdb());
        existingMovie.setType(movie.getType());
//        existingMovie.setTomatoes(movie.getTomatoes());
        return movieRepository.save(existingMovie);
    }



}
