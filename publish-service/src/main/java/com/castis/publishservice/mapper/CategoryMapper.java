package com.castis.publishservice.mapper;

import com.castis.publishservice.dto.queue.CategoryRequest;
import com.castis.publishservice.entity.Category;
import com.castis.publishservice.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {Utils.class})
public interface CategoryMapper {
    public static final CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    @Mapping(target = "name", source = "categoryName")
    CategoryRequest toRequest(Category entity);
}