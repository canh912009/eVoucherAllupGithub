package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.Store;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface StoreRepository extends JpaRepository<Store, String>, StoreRepositoryCustom {

    Optional<Store> findByIdAndValidYn(String id, EnumValidYn validYn);
    @Query(value = "select max(store_id) as store_id, brand_id from tb_store where brand_id in :brandIds group by brand_id", nativeQuery = true)
    List<Map<String, Object>> getMaxStoreIdByBrandIdIn(Collection<String> brandIds);

    List<Store> findAllByBrandId(String brandId);
    List<Store> findAllByStoreCodeInAndValidYn(List<String> storeCodes, EnumValidYn validYn);
    List<Store> findAllByBrandIdInAndValidYn(Collection<String> brandIds, EnumValidYn validYn);

    List<Store> findAllByBrandIdAndValidYn(String brandId, EnumValidYn validYn);

    @Query(value = "SELECT s.* " +
                        "FROM tb_store s " +
                        "LEFT JOIN tb_brand b ON s.brand_id = b.brand_id " +
                        "LEFT JOIN tb_supplier su ON b.supplier_id = su.supplier_id " +
                        "WHERE su.supplier_id = :supplierId AND s.valid_yn = :validYn", nativeQuery = true)
    List<Store> findAllBySupplierIdAndValidYn(String supplierId, String validYn);

    @Query(value = "select id from Store where id like ?1 order by id asc")
    List<String> findAllStoreIdByRegexIdAndIdASC(String regexId);

    List<Store> findAllByIdIn(List<String> storeIds);
}
