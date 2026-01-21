package com.evoucher.adminapi.cms.mapper;

import com.evoucher.adminapi.cms.dao.models.BulkCategory;
import com.evoucher.adminapi.cms.dao.models.Category;
import com.evoucher.adminapi.cms.dao.models.Store;
import com.evoucher.adminapi.cms.service.models.BulkCategoryDTO;
import com.evoucher.adminapi.cms.service.models.CategoryDTO;
import com.evoucher.adminapi.cms.service.models.StoreDTO;
import com.evoucher.adminapi.cms.service.models.request.CategoryRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.LinkedList;
import java.util.List;

@Mapper(builder = @Builder(disableBuilder = true), uses = {CategoryMapper.class,BrandMapper.class, GoodsMapper.class})
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toEntity(CategoryDTO categoryDTO);

    Category toEntity(CategoryRequest categoryRequest);

    CategoryDTO toDTO(Category category);

    List<CategoryDTO> toListDTO(List<Category> categories);
    LinkedList<BulkCategory> toListEntityBulkCategory(LinkedList<BulkCategoryDTO> bulkCategoryDTOS);

    List<Category> toListEntity(List<CategoryDTO> categoryDTOS);
    BulkCategoryDTO toDTO(BulkCategory entity);
    BulkCategory toEntity(BulkCategoryDTO dTO);
}
