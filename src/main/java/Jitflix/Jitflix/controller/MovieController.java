package Jitflix.Jitflix.controller;

import Jitflix.Jitflix.entity.Movie;
import Jitflix.Jitflix.s3.S3Buckets;
import Jitflix.Jitflix.s3.S3Service;
import Jitflix.Jitflix.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;

    public MovieController(MovieService movieService, S3Service s3Service, S3Buckets s3Buckets) {
        this.movieService = movieService;
        this.s3Service = s3Service;
        this.s3Buckets = s3Buckets;
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




    @GetMapping("/watch/{movieId}/playlist")
    public ResponseEntity<?> getPlaylist(@PathVariable String movieId) throws IOException{
        Movie movie = movieService.getMovieById(movieId);
//        String movieLocation = movie.getBucketUrl();
        System.out.println(movie);
        if (movie == null) {
            return ResponseEntity.ok("Movie not found");
        }

        byte[] playlistResource =  s3Service.getObject(s3Buckets.getBucket(), movieId +"/output_playlist.m3u8");
        if (playlistResource.length > 0) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"))
                    .body(playlistResource);
        } else {
            return ResponseEntity.ok("Master playlist not found");
        }
    }

    @GetMapping("/watch/{movieId}/{segment}.ts")
    public ResponseEntity<byte[]> getSegment(@PathVariable String movieId, @PathVariable String segment) throws IOException {
        Movie movie = movieService.getMovieById(movieId);
        if (movie == null) {
            return ResponseEntity.ok("Movie not found".getBytes());
        }

        byte[] segmentResource =  s3Service.getObject(s3Buckets.getBucket(), movieId + "/"  + segment + ".ts");

        if (segmentResource.length > 0) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/MP2T"))
                    .body(segmentResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
