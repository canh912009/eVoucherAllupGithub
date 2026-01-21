package com.evoucher.adminapi.service.spring.running;


import com.evoucher.adminapi.AdminApiApplication;
import com.evoucher.adminapi.cms.dao.CategoryRepository;
import com.evoucher.adminapi.cms.service.CategoryService;
import com.evoucher.adminapi.cms.service.models.CategoryDTO;
import com.evoucher.adminapi.cms.service.models.request.CategoryRequest;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = AdminApiApplication.class)
@EnabledIfEnvironmentVariable(named = "TEST_TYPE", matches = "FULL_TEST")
@Slf4j
public class CategoryServiceTest {
    @Autowired
    private CategoryService service;
    @Autowired
    private CategoryRepository repository;

    @Test
    public void createNew_thenReturnSavedCategory() {
        CategoryRequest request = CategoryRequest.builder()
                .categoryCode("TEST_CATEGORY_5")
                .categoryName("Wooden")
                .build();

        CategoryDTO saved = service.createCategory(request);

        CategoryDTO found = service.findDtoById("TEST_CATEGORY_5");

        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(saved.getCategoryCode()).isEqualTo(found.getCategoryCode());

    }

    @Test
    public void createWithExistedId_ThenThrowError() {
        CategoryRequest request = CategoryRequest.builder()
                .categoryCode("TEST_CATEGORY_5")
                .categoryName("Wooden2")
                .build();

        CustomCodeException exception = assertThrows(CustomCodeException.class, () ->
                service.createCategory(request));

        assertEquals("Category code already exists.", exception.getMessage());
    }

    @Test
    public void update_ThenReturnUpdatedCategory() {
        CategoryRequest request = CategoryRequest.builder()
                .categoryCode("TEST_CATEGORY_5")
                .categoryName("Wooden2")
                .validYn(EnumValidYn.Y)
                .build();

        CategoryDTO response = service.updateCategory("TEST_CATEGORY_5", request);
        CategoryDTO saved = service.findDtoById("TEST_CATEGORY_5");

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(response.getCategoryCode()).isEqualTo(saved.getCategoryCode());
        Assertions.assertThat(response.getCategoryName()).isEqualTo(saved.getCategoryName());
        Assertions.assertThat(response.getCategoryCode()).isEqualTo(request.getCategoryCode());
        Assertions.assertThat(response.getCategoryName()).isEqualTo(request.getCategoryName());
    }

    @Test
    public void delete_ThenCanNotFindFromDB() {
        String categoryCode = "TEST_CATEGORY_5";

        String response = service.delete(categoryCode);

        Assertions.assertThat(response).isNotNull().isEqualTo(categoryCode);

        CategoryDTO saved = service.findDtoById(categoryCode);
        Assertions.assertThat(saved.getValidYn()).isEqualTo(EnumValidYn.N);
    }
}
