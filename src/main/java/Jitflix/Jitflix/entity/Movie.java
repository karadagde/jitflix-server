package Jitflix.Jitflix.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Document(collection="movies")
public class Movie {
    @Id
    private String id;

    private String bucketUrl;
    private String poster;
    private String plot;
    private List<String> genres;
    private Number runtime;
    private List<String> cast;
    private int num_mflix_comments;
    private String title;
    private String fullplot;
    private List<String> countries;
    private Date released;
    private List<String> directors;
    private List<String> writers;
    private String rated;
//    private String awards;
    private String lastupdated;
    private int year;
//    private String imdb;
    private String type;


}
