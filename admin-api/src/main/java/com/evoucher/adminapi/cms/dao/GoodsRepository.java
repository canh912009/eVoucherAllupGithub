package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.service.models.vnptEPay.GoodPurchaseInfo;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SystemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface GoodsRepository extends JpaRepository<Goods, Integer>, GoodsRepositoryCustom {
    Optional<Goods> findByIdAndValidYn(Integer id, EnumValidYn validYn);
    Optional<List<Goods>> findAllByIdIn(Collection<Integer> ids);
    List<Goods> findAllBySupplierGoodsIdIn(Collection<String> goodCodes);

    Long countAllByBrandId(String brandId);
    List<Goods> findAllByBrandId(String brandId);
    Optional<List<Goods>> findAllByBrandIdIn(Collection<String> brandId);

    @Query("SELECT g, s, b " +
           "FROM Goods g " +
           "LEFT JOIN Supplier s ON g.supplierId = s.id " +
           "LEFT JOIN Brand b ON g.brandId = b.id " +
           "WHERE g.id IN :listId")
    List<Tuple> findGoodsSupplierBrandByIdIn(@Param(value = "listId") Collection<Integer> listId);
    @Query(value = "select tg.sell_price as sellPrice, tb.brand_nm as provider from tb_goods tg inner join tb_brand tb on tg.brand_id = tb.brand_id where tg.goods_id = ?1 and tb.supplier_id = ?2", nativeQuery = true)
    GoodPurchaseInfo getGoodPurchaseInfoByGoodId(Long goodId, String supplierId);

    @Query("SELECT g " +
            "FROM Goods g " +
            "LEFT JOIN GoodsChoice gc ON g.id = gc.goodsId " +
            "WHERE gc.parentGoodsId = :id AND gc.validYn = 'Y' order by gc.displayIndex")
    LinkedList<Goods> findGoodsChoiceById(@Param(value = "id") Integer id);

    boolean existsByIdAndSystem(Integer id, SystemType systemType);

    boolean existsBySupplierGoodsId(String supplierGoodsId);
}
