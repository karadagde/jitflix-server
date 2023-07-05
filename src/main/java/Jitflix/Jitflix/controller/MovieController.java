package Jitflix.Jitflix.controller;

import Jitflix.Jitflix.entity.Movie;
import Jitflix.Jitflix.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }




    @GetMapping("/all")
    public Page<Movie> getAllMovies() {

        return movieService.getAllMovies();

    }

    @GetMapping("/{movieId}")
    public Movie getMovieById(@PathVariable String movieId)
    {
        return movieService.getMovieById(movieId);
    }

    @GetMapping("/title/{title}")
        public List<Movie> getMovieByTitle(@PathVariable String title){
        return movieService.getMovieByTitle(title);
    }

}
