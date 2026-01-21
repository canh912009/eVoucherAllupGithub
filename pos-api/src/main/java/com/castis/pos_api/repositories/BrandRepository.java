package com.castis.pos_api.repositories;

import com.castis.pos_api.entity.Brand;
import com.castis.pos_api.enum_constant.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
    Optional<Brand> findByAppIdAndValidYn(String id, EnumValidYn validYn);
}
