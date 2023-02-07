package eu.jev.springwebfluxrest.controllers;

import eu.jev.springwebfluxrest.domain.Category;
import eu.jev.springwebfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CategoryControllerTest {

    CategoryRepository categoryRepository;

    CategoryController categoryController;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void getAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Flux.just(
                Category.builder()
                        .description("Cat1")
                        .build(),
                Category.builder()
                        .description("Cat2")
                        .build()
        ));
        webTestClient.get()
                .uri(("/api/v1/categories"))
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getCategoryById() {
        when(categoryRepository.findById(anyString())).thenReturn(Mono.just(
                Category.builder()
                        .description("Cat")
                        .build()
        ));
        webTestClient.get()
                .uri("/api/v1/categories/1")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    void createCategory() {
        when(categoryRepository.saveAll(any(Publisher.class))).thenReturn(Flux.just(
                Category.builder().description("Some Cat").build()
        ));

        Mono<Category> categoryToSave = Mono.just(Category.builder().description("Some Cat").build());

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(categoryToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }
}