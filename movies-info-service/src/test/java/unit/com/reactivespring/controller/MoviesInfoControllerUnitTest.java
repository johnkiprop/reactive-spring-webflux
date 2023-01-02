package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.services.MoviesInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;



@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
public class MoviesInfoControllerUnitTest {
@Autowired private WebTestClient webTestClient;
@MockBean private MoviesInfoService moviesInfoServiceMock;
    static final String MOVIE_URL="/v1/movieinfos";
    @Test
    void getAllMoviesInfo(){
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));
        when(moviesInfoServiceMock.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfos));


        webTestClient
                .get()
                .uri(MOVIE_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void addMovieInfo() {
        var movieInfo = new MovieInfo(null, "Batman Begins Test",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(moviesInfoServiceMock.addMoviesInfo(isA(MovieInfo.class))).thenReturn(
                Mono.just(new MovieInfo("mockId", "Batman Begins Test",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );
        webTestClient
                .post()
                .uri(MOVIE_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert savedMovieInfo !=null;
                    assert savedMovieInfo.getMovieInfoId() !=null;
                    assertEquals("mockId",savedMovieInfo.getMovieInfoId());
                });
    }

    @Test
    void addMovieInfoValidation() {
        var movieInfo = new MovieInfo(null, "",
                -2005, List.of(""), LocalDate.parse("2005-06-15"));

        webTestClient
                .post()
                .uri(MOVIE_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var response = stringEntityExchangeResult.getResponseBody();
                    System.out.println("Response is :"+ response);
                    var expectedMessage = "Movie Info cast must be present,Movie Info name must be present,Year must be a positive integer";
                    assertNotNull(response);
                    assertEquals(expectedMessage, response);
                });
//
    }
    @Test
    void updateMovieInfo() {
        var movieInfoId="abc";
        var movieInfo = new MovieInfo(null, "Batman Begins Test Update",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        when(moviesInfoServiceMock.updateMoviesInfo(isA(MovieInfo.class),isA(String.class))).thenReturn(
                Mono.just(new MovieInfo(movieInfoId, "Batman Begins Test Update",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );
        webTestClient
                .put()
                .uri(MOVIE_URL+"/{id}",movieInfoId )
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var updateMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert updateMovieInfo !=null;
                    assert updateMovieInfo.getMovieInfoId() !=null;
                    assertEquals("Batman Begins Test Update", updateMovieInfo.getName());
                });
    }


}
