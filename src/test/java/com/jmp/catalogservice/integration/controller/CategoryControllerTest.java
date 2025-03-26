package com.jmp.catalogservice.integration.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jmp.catalogservice.dto.request.CategoryRequest;
import com.jmp.catalogservice.dto.response.CategoryResponse;
import com.jmp.catalogservice.support.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CategoryControllerTest extends BaseIntegrationTest {

    private static final String BASE_URI = "/api/v1/categories";
    private static final String TREE_URI = BASE_URI + "/{id}/tree";
    private static final String ROOT_CATEGORIES_URI = BASE_URI + "/parents";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DataSet(cleanBefore = true)
    void shouldCreateCategorySuccessfully() {
        CategoryRequest request = new CategoryRequest("UniqueCategory", null);

        webTestClient.post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CategoryResponse.class)
                .value(response -> {
                    assertThat(response.getId()).isNotNull();
                    assertThat(response.getName()).isEqualTo("UniqueCategory");
                    assertThat(response.getChildren()).isNull();
                });
    }

    @Test
    @DataSet(executeScriptsBefore = "dataset/user/categories.sql", cleanBefore = true)
    void shouldGetCategoryByIdSuccessfully() {
        webTestClient.get()
                .uri(BASE_URI + "/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .value(response -> {
                    assertThat(response.getId()).isEqualTo(1L);
                    assertThat(response.getName()).isEqualTo("Electronics");
                });
    }

    @Test
    @DataSet(executeScriptsBefore = "dataset/user/categories.sql", cleanBefore = true)
    void shouldUpdateCategorySuccessfully() {
        CategoryRequest request = new CategoryRequest("Updated Electronics", null);

        webTestClient.put()
                .uri(BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .value(response -> {
                    assertThat(response.getId()).isEqualTo(1L);
                    assertThat(response.getName()).isEqualTo("Updated Electronics");
                });
    }

    @Test
    @DataSet(executeScriptsBefore = "dataset/user/categories.sql", cleanBefore = true)
    void shouldReturnConflictWhenCircularDependency() {
        CategoryRequest request = new CategoryRequest("Electronics", 1L);
        webTestClient.put()
                .uri(BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Category cannot be a parent of itself");


    }

    @Test
    @DataSet(executeScriptsBefore = "dataset/user/categories.sql", cleanBefore = true)
    void shouldDeleteCategorySuccessfully() {
        webTestClient.delete()
                .uri(BASE_URI + "/1")
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri(BASE_URI + "/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DataSet(executeScriptsBefore = "dataset/user/categories-with-children.sql", cleanBefore = true)
    void shouldGetCategoryTreeSuccessfully() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(TREE_URI).build(1L))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .value(response -> {
                    assertThat(response.getId()).isEqualTo(1L);
                    assertThat(response.getName()).isEqualTo("Electronics");
                    assertThat(response.getChildren())
                            .hasSize(2)
                            .extracting(CategoryResponse::getName)
                            .containsExactlyInAnyOrder("Laptops", "Smartphones");
                });
    }

    @Test
    @DataSet(executeScriptsBefore = "dataset/user/categories-with-children.sql", cleanBefore = true)
    void shouldGetAllRootCategoriesSuccessfully() {
        webTestClient.get()
                .uri(ROOT_CATEGORIES_URI)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CategoryResponse.class)
                .value(categories -> assertThat(categories)
                        .hasSize(1)
                        .extracting(CategoryResponse::getName)
                        .containsExactly("Electronics"));
    }

    @Test
    @DataSet(executeScriptsBefore = "dataset/user/categories.sql", cleanBefore = true)
    void shouldReturnConflictWhenCreatingDuplicateCategory() {
        CategoryRequest request = new CategoryRequest("Electronics", null);

        webTestClient.post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Duplicate category name detected: Electronics");
    }

    @Test
    void shouldReturnNotFoundForNonExistingCategory() {
        webTestClient.get()
                .uri(BASE_URI + "/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DataSet(executeScriptsBefore = "dataset/user/categories.sql", cleanBefore = true)
    void shouldReturnBadRequestForInvalidInput() {
        CategoryRequest invalidRequest = new CategoryRequest("", null);

        webTestClient.post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DataSet(executeScriptsBefore = "dataset/user/categories-with-children.sql", cleanBefore = true)
    void shouldReturnConflictWhenUpdatingWithDuplicateName() {
        CategoryRequest request = new CategoryRequest("Electronics", null);

        webTestClient.put()
                .uri(BASE_URI + "/2")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingCategory() {
        webTestClient.delete()
                .uri(BASE_URI + "/999")
                .exchange()
                .expectStatus().isNotFound();
    }
}
