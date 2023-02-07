package eu.jev.springwebfluxrest.controllers;

import eu.jev.springwebfluxrest.domain.Vendor;
import eu.jev.springwebfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
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
        webTestClient.get().uri("/api/v1/vendors")
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
        webTestClient.get().uri("/api/v1/vendors/1")
                .exchange()
                .expectBody(Vendor.class);
    }
}