package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.services.MoviesInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class MoviesInfoController {
    private MoviesInfoService moviesInfoService;

    public MoviesInfoController(MoviesInfoService moviesInfoService) {
        this.moviesInfoService = moviesInfoService;
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody MovieInfo movieInfo){
       return moviesInfoService.addMoviesInfo(movieInfo).log();
    }
    @PutMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> updateMovieInfo(@RequestBody MovieInfo updatedMovieInfo,@PathVariable String id){
        return moviesInfoService.updateMoviesInfo(updatedMovieInfo,id).log();
    }
    @GetMapping("/movieinfos")
    public Flux<MovieInfo> getAllMovieInfos(){
        return moviesInfoService.getAllMovieInfos().log();
    }
    @GetMapping("/movieinfos/{id}")
    public Mono<MovieInfo> getMovieInfoById(@PathVariable String id){
        return moviesInfoService.getMovieInfosById(id).log();
    }
    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfo(@PathVariable String id){
        return moviesInfoService.deleteMovieInfo(id).log();
    }
}
