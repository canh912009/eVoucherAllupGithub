package com.evoucher.adminapi.cms.service;


import com.evoucher.adminapi.cms.dao.CategoryRepository;
import com.evoucher.adminapi.cms.dao.models.BulkCategory;
import com.evoucher.adminapi.cms.dao.models.Category;
import com.evoucher.adminapi.cms.mapper.CategoryMapper;
import com.evoucher.adminapi.cms.service.models.BulkCategoryDTO;
import com.evoucher.adminapi.cms.service.models.CategoryDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.CategoryRequest;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.service.EntityService;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService extends EntityService<Category, String, CategoryDTO> {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public JpaRepository<Category, String> getRepository() {
        return categoryRepository;
    }

    @Override
    public String getEntityType() {
        return "category";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException(
                MessageUtils.getMessage("evoucher.category.not.found"));
    }

    @Override
    public Category toEntity(CategoryDTO dto) {
        return categoryMapper.toEntity(dto);
    }

    @Override
    public CategoryDTO toDto(Category entity) {
        return categoryMapper.toDTO(entity);
    }

    public CategoryDTO createCategory(CategoryRequest categoryRequest) throws CustomCodeException {
        log.info("save category: {}", categoryRequest);
        Category existed;
        try {
            existed = findById(categoryRequest.getCategoryCode());
            if (existed.getValidYn() == EnumValidYn.Y)
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.category.exists"), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            log.info("category not exist, create new");
        }
        Category category = this.toEntity(categoryRequest);
        category.setValidYn(EnumValidYn.Y);
        log.info("Save Category with categoryId: {}", category.getCategoryCode());
        return this.toDto(save(category));
    }

    public CategoryDTO updateCategory(String id, CategoryRequest categoryRequest) throws CustomCodeException {
        log.info("Find Category with categoryId: {}", id);
        CategoryDTO category = findDtoById(id);


        // make sure id in path variable is equal to request id
        if (!Objects.equals(id, categoryRequest.getCategoryCode())) {
            log.error("id and category request id is not equals");
            throw new CustomCodeException(
                    "id of request must be equal to id of path variable",
                    HttpStatus.BAD_REQUEST
            );
        }
//        validateValid(category);

        log.info("Update Category with categoryId: {}", category.getCategoryCode());
        Category entity = this.toEntity(categoryRequest);
//        entity.setValidYn(EnumValidYn.Y);
        return categoryMapper.toDTO(this.save(entity));
    }

    public String delete(String id) throws CustomCodeException{
        log.info("Find Category with categoryId: {}", id);
        Category category = this.findById(id);

        validateValid(category);

        category.setValidYn(EnumValidYn.N);
        log.info("Delete Category with categoryId: {}", category.getCategoryCode());
        categoryRepository.save(category);
        return id;
    }

    public Page<CategoryDTO> searchCategory(FilterSearchCms filterSearchCms) {
        log.info("Search Category: {}", filterSearchCms);
        int page = ObjectUtils.isEmpty(filterSearchCms.getPage()) ? 0 : filterSearchCms.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchCms.getPageSize()) ? 10 : filterSearchCms.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        List<CategoryDTO> list = new ArrayList<>();
        Long countContractDTOS = categoryRepository.countCategory(filterSearchCms, pageable);
        if (countContractDTOS > 0) {
            list = categoryRepository.searchCategory(filterSearchCms, pageable)
                    .stream().map(this::toDto).collect(Collectors.toList());

        }

        return new PageImpl<>(
                list,
                pageable,
                countContractDTOS);
    }

    private void validateValid(Category entity) throws CustomCodeException{
        if (entity.getValidYn() != EnumValidYn.Y) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.category.is.invalid"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void validateValid(CategoryDTO dto) throws CustomCodeException{
        if (dto.getValidYn() != EnumValidYn.Y) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.category.is.invalid"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public void validateExistByIdIn(Collection<String> ids) throws EntityNotFoundException {
        List<String> existedIds = this.findAllByIdIn(ids).stream().map(Category::getCategoryCode).collect(Collectors.toList());
        ids.forEach(o -> {
            if (!existedIds.contains(o)) {
                throw this.getNotFoundException(o);
            }
        });
    }
    public Category toEntity(CategoryRequest request) {
        return categoryMapper.toEntity(request);
    }
    public List<BulkCategory> toListEntityBulkCategory(LinkedList<BulkCategoryDTO> dtoList) {
        return categoryMapper.toListEntityBulkCategory(dtoList);
    }
    public BulkCategoryDTO toDto(BulkCategory entity) {
        return categoryMapper.toDTO(entity);
    }
}
