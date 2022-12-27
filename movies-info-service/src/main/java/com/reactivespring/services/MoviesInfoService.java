package com.reactivespring.services;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MoviesInfoService {
    private MovieInfoRepository movieInfoRepository;

    public MoviesInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> addMoviesInfo(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo);
    }

    public Flux<MovieInfo> getAllMovieInfos() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getMovieInfosById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<MovieInfo> updateMoviesInfo(MovieInfo updatedMovieInfo, String id) {
     return  movieInfoRepository.findById(id)
               .flatMap(movieInfo -> {
                   movieInfo.setCast(updatedMovieInfo.getCast());
                   movieInfo.setName(updatedMovieInfo.getName());
                   movieInfo.setReleaseDate(updatedMovieInfo.getReleaseDate());
                   movieInfo.setYear(updatedMovieInfo.getYear());
                   return movieInfoRepository.save(movieInfo);
               });
    }

    public Mono<Void> deleteMovieInfo(String id) {
        return movieInfoRepository.deleteById(id);
    }
}

