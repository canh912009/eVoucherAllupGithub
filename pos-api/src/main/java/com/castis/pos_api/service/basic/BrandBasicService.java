package com.castis.pos_api.service.basic;

import com.castis.pos_api.dto.BrandDto;
import com.castis.pos_api.entity.Brand;
import com.castis.pos_api.exception.ApplicationException;
import com.castis.pos_api.mapper.BrandMapper;
import com.castis.pos_api.service.common.EntityService;
import com.castis.pos_api.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandBasicService extends EntityService<Brand, String, BrandDto> {

    private final JpaRepository<Brand, String> repository;
    private static final BrandMapper MAPPER = BrandMapper.INSTANCE;

    @Override
    public JpaRepository<Brand, String> getRepository() {
        return repository;
    }

    @Override
    public String getEntityType() {
        return "brand";
    }

    @Override
    public EntityNotFoundException getNotFoundException(String id) {
        return new EntityNotFoundException("can not find brand by id: " + id);
    }

    @Override
    public Brand toEntity(BrandDto dto) {
        return MAPPER.toEntity(dto);
    }

    @Override
    public BrandDto toDto(Brand entity) {
        return MAPPER.toDto(entity);
    }

    public BrandDto findDtoById(String id) {
        try {
            return toDto(findById(id));
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E4404_NOT_FOUND.getCode(), "Brand not found");
        }
    }
}
