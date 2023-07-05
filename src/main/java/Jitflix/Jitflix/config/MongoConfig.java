package Jitflix.Jitflix.config;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import Jitflix.Jitflix.entity.Movie;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;


@Configuration
public class MongoConfig {
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(new MovieReader()));
    }

    @ReadingConverter
    static class MovieReader implements Converter<Document, Movie> {
        @Override
        public Movie convert(Document source) {
            Movie movie = new Movie();
            System.out.println(source.getString("id"));
            System.out.println(source);

            movie.setId(source.getObjectId("_id") != null ? source.getObjectId("_id").toHexString() : "");

            movie.setBucketUrl(source.getString("bucketUrl") != null ? source.getString("bucketUrl") : "Not Available");
            movie.setPoster(source.getString("poster") != null ? source.getString("poster") : "");
            movie.setPlot(source.getString("plot") != null ? source.getString("plot") : "");
            movie.setGenres(source.get("genres", List.class) != null ? source.get("genres", List.class) : Arrays.asList(""));
            movie.setRuntime(source.getInteger("runtime") != null ? source.getInteger("runtime") : 0);
            movie.setCast(source.get("cast", List.class) != null ? source.get("cast", List.class) : Arrays.asList(""));
            movie.setNum_mflix_comments(source.getInteger("num_mflix_comments") != null ? source.getInteger("num_mflix_comments") : 0);
            movie.setFullplot(source.getString("fullplot") != null ? source.getString("fullplot") : "");
            movie.setCountries(source.get("countries", List.class) != null ? source.get("countries", List.class) : Arrays.asList(""));
            movie.setDirectors(source.get("directors", List.class) != null ? source.get("directors", List.class) : Arrays.asList(""));
            movie.setWriters(source.get("writers", List.class) != null ? source.get("writers", List.class) : Arrays.asList(""));
            movie.setRated(source.getString("rated") != null ? source.getString("rated") : "");
//            movie.setAwards(source.getString("awards") != null ? source.getString("awards") : "");
            movie.setYear(source.getInteger("year") != null ? source.getInteger("year") : 0);
//            movie.setImdb(source.getString("imdb") != null ? source.getString("imdb") : "");
            movie.setType(source.getString("type") != null ? source.getString("type") : "");
//            movie.setTomatoes(source.getString("tomatoes") != null ? source.getString("tomatoes") : "");
            movie.setReleased(source.getDate("released") != null ? source.getDate("released") : new Date(0));
            movie.setLastupdated(source.getString("lastupdated") != null ? source.getString("lastupdated") : "");

            return movie;
        }
    }

}
