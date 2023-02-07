package eu.jev.springwebfluxrest.controllers;

import eu.jev.springwebfluxrest.domain.Category;
import eu.jev.springwebfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CategoryController.BASE_URL)
public class CategoryController {
    public static final String BASE_URL = "/api/v1/categories";

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    Flux<Category> getAllCategories() {
       return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Category> getCategoryById(@PathVariable String id) {
      return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Mono<Void> createCategory(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }

    @PutMapping("/{id}")
    Mono<Category> updateCategory(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/{id}")
    Mono<Category> patchCategory(@PathVariable String id, @RequestBody Category category) {

        Category foundCategory = categoryRepository.findById(id).block();

        if (foundCategory.getDescription() != category.getDescription()) {
            foundCategory.setDescription(category.getDescription());
            return categoryRepository.save(category);
        }
        return Mono.just(foundCategory);
    }
}
