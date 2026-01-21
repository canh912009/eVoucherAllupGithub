package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SystemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String>, BrandRepositoryCustom {

    boolean existsByIdAndValidYn(String id, EnumValidYn validYn);
    List<Brand> findAllBySystemAndValidYn(SystemType systemType, EnumValidYn valid);
    Optional<Brand> findByIdAndValidYn(String id, EnumValidYn validYn);

    @Query(value = "select id from Brand where id like ?1 order by id asc")
    List<String> findAllBrandIdByBrandIdOrderByIdASC(String regexId);

    List<Brand> findAllBySupplierIdAndValidYn(String supplierId, EnumValidYn enumValidYn);

    @Query(value = "SELECT b.* " +
                   "FROM tb_brand b " +
                   "LEFT JOIN tb_goods tg ON b.brand_id = tg.brand_id " +
                   "WHERE tg.goods_id IN :goodsIds GROUP BY b.brand_id",
           nativeQuery = true)
    List<Brand> findAllByGoodsIdIn(Collection<Integer> goodsIds);

    boolean existsByBrandCode(String brandCode);

    Optional<Brand> findByAppId(String appId);
}
