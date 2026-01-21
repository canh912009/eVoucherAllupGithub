package asia.castis.evoucher.api.repository;

import asia.castis.evoucher.api.common.enums.EnumValidYn;
import asia.castis.evoucher.api.entity.GoodsChoice;
import asia.castis.evoucher.api.entity.GoodsChoiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsChoiceRepository extends JpaRepository<GoodsChoice, GoodsChoiceId> {
    List<GoodsChoice> findByParentGoodsIdAndValidYn(Integer parentGoodsId, EnumValidYn validYn);
}
