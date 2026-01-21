package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.entity.BulkCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkCategoryRepository extends JpaRepository<BulkCategory, Long> {
    /**
     * <pre>
     *     CONDITION
     *     - Bulk category VALID
     *     - Category VALID
     *     - Bulk category has children
     *     ORDER
     *     - By display index
     * </pre>
     * @return bulk category pages
     */
    @Query("SELECT bc FROM BulkCategory bc" +
            " JOIN bc.category c" +
            " WHERE bc.goodsId = :goodsId" +
            " AND bc.validYn = 'Y'" +
            " AND c.validYn = 'Y'" +
            " AND (SELECT COUNT(bb.bulkBrandId) FROM BulkBrand bb WHERE bb.validYn = 'Y' AND bb.bulkCtgrId = bc.bulkCtgrId) > 0"
    )
    Page<BulkCategory> findValidCategory_ByGoodsId(int goodsId, Pageable pageable);

    /**
     * <pre>
     *     CONDITION
     *     - Bulk category VALID
     *     - Category VALID
     *     - Category Name LIKE :name
     *     - Bulk category has children
     *     ORDER
     *     - By display index
     * </pre>
     * @return bulk category pages
     */
    @Query("SELECT bc FROM BulkCategory bc" +
            " JOIN bc.category c" +
            " WHERE bc.goodsId = :goodsId" +
            " AND c.categoryName LIKE %:name%" +
            " AND bc.validYn = 'Y'" +
            " AND c.validYn = 'Y'" +
            " AND (SELECT COUNT(bb.bulkBrandId) FROM BulkBrand bb WHERE bb.validYn = 'Y' AND bb.bulkCtgrId = bc.bulkCtgrId) > 0"
    )
    Page<BulkCategory> findValidCategory_ByGoodsIdAndCategoryName(int goodsId, String name, Pageable pageable);

}
