package com.reactive.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {


    private final WebTestClient webTestClient;

    @Autowired
    FluxAndMonoControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }


    @Test
    public void flux(){
        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(4);

    }

    @Test
    public void flux_approach2(){
        var exchangeResult = webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(exchangeResult)
                .expectNext(1,2,3,4)
                .verifyComplete();

    }

    @Test
    public void flux_approach3(){

        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                    var responseBody = listEntityExchangeResult.getResponseBody();
                    assert responseBody.size()==4;
                });
    }

    @Test
    public void mono(){

        webTestClient.get()
                .uri("/mono")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith(listEntityExchangeResult -> {
                    String responseBody = listEntityExchangeResult.getResponseBody();
                    assertEquals(responseBody,"Hello World");
                });
    }

    @Test
    public void stream(){

        var exchangeResult = webTestClient.get()
                .uri("/stream")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(exchangeResult)
                .expectNext(0L,1L, 2L, 3L)
                .thenCancel()
                .verify();
    }
}