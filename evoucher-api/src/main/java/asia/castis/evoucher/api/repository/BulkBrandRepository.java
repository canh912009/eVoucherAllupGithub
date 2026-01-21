package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.entity.BulkBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BulkBrandRepository extends JpaRepository<BulkBrand, Long> {

    /**
     * <pre>
     *     CONDITION
     *     - Bulk brand VALID
     *     - Brand VALID
     *     - Bulk brand has children
     *     ORDER
     *     - By display index
     * </pre>
     * @return bulk brand pages
     */
    @Query("SELECT bu FROM BulkBrand bu" +
            " JOIN bu.brand b" +
            " WHERE bu.bulkCtgrId = :bulkCategoryId" +
            " AND bu.validYn = 'Y'" +
            " AND b.validYn='Y'" +
            " AND 0 < (SELECT COUNT(bg.bulkBrandId) FROM BulkGoods bg WHERE bg.validYn = 'Y' AND bg.bulkBrandId = bu.bulkBrandId)"
    )
    Page<BulkBrand> findVlidBrand_ByBulkCtgrId(long bulkCategoryId, Pageable pageable);

    /**
     * <pre>
     *     CONDITION
     *     - Bulk brand VALID
     *     - Bulk brand has children
     *     - Brand VALID
     *     - Brand like :name
     *     ORDER
     *     - By display index
     * </pre>
     * @return bulk brand pages
     */
    @Query("SELECT bu FROM BulkBrand bu" +
            " JOIN bu.brand b" +
            " WHERE bu.bulkCtgrId = :bulkCategoryId" +
            " AND b.brandName LIKE %:name%" +
            " AND bu.validYn = 'Y'" +
            " AND b.validYn='Y'" +
            " AND 0 < (SELECT COUNT(bg.bulkBrandId) FROM BulkGoods bg WHERE bg.validYn = 'Y' AND bg.bulkBrandId = bu.bulkBrandId)"
    )
    Page<BulkBrand> findValidBrand_ByBulkCtgrIdAndBrandName(long bulkCategoryId, String name, Pageable pageable);
}
