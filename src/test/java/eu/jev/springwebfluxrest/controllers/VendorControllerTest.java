package eu.jev.springwebfluxrest.controllers;

import eu.jev.springwebfluxrest.domain.Vendor;
import eu.jev.springwebfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class VendorControllerTest {

    VendorRepository vendorRepository;

    VendorController vendorController;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void getAllVendors() {
        when(vendorRepository.findAll()).thenReturn(Flux.just(
                Vendor.builder()
                        .firstname("Vendor 1 Firstname")
                        .lastname("Vendor 1 Lastname")
                        .build(),
                Vendor.builder()
                        .firstname("Vendor 2 Firstname")
                        .lastname("Vendor 2 Lastname")
                        .build()
        ));
        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getVendorByID() {
        when(vendorRepository.findById(anyString())).thenReturn(Mono.just(
                Vendor.builder()
                        .firstname("Vendor Firstname")
                        .lastname("Vendor Lastname")
                        .build()
        ));
        webTestClient.get()
                .uri("/api/v1/vendors/1")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void createVendor() {
        when(vendorRepository.saveAll(any(Publisher.class))).thenReturn(Flux.just(
                Vendor.builder().firstname("Some Firstname").lastname("Some Lastname").build()
        ));

        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstname("Some Firstname").lastname("Some Lastname").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void updateVendor() {
        when(vendorRepository.save(any(Vendor.class))).thenReturn(Mono.just(
                Vendor.builder().firstname("Some Firstname").lastname("Some Latname").build()
        ));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().firstname("Some Firstname").lastname("Some Latname").build());

        webTestClient.put()
                .uri("/api/v1/vendors/1")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}