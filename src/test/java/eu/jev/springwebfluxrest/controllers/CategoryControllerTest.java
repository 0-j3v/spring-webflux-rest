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
import static org.mockito.Mockito.*;

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

    @Test
    void updateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(Mono.just(
                Category.builder().description("Update Cat").build()
        ));

        Mono<Category> categoryToUpdate = Mono.just(Category.builder().description("Update Cat").build());

        webTestClient.put()
                .uri("/api/v1/categories/1")
                .body(categoryToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void patchCategoryWithChanges() {
        when(categoryRepository.findById(anyString())).thenReturn(Mono.just(Category.builder().build()));
        when(categoryRepository.save(any(Category.class))).thenReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToPatch = Mono.just(Category.builder().description("New Description").build());

        webTestClient.patch()
                .uri("/api/v1/categories/1")
                .body(categoryToPatch, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    void patchCategoryWithNoChanges() {
        when(categoryRepository.findById(anyString())).thenReturn(Mono.just(Category.builder().build()));
        when(categoryRepository.save(any(Category.class))).thenReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToPatch = Mono.just(Category.builder().build());

        webTestClient.patch()
                .uri("/api/v1/categories/1")
                .body(categoryToPatch, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, never()).save(any());
    }
}