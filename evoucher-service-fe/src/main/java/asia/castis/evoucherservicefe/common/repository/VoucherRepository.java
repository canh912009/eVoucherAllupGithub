package asia.castis.evoucherservicefe.common.repository;

import asia.castis.evoucherservicefe.common.model.VoucherModel;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


public interface VoucherRepository extends ElasticsearchRepository<VoucherModel, String> {
    @Query("{\"bool\":{\"must\":[{\"range\":{\"createDate\":{\"lt\":\"?0\"}}},{\"bool\":{\"must_not\":[{\"match\":{\"voucherStatus\":\"EXPIRE\"}},{\"match\":{\"voucherStatus\":\"USED\"}}]}}]}}")
    List<VoucherModel> findExpiredVouchers(String createDate);
    @Query("{\"bool\":{\"must\":[{\"range\":{\"createDate\":{\"lt\":\"?0\"}}},{\"match\":{\"transferStatus\":\"RECPT_WAIT\"}}]}}")
    List<VoucherModel> findUnClaimedVouchers(String createDate);

    VoucherModel findBySerialNo(@NotNull String serialNumber);
    Optional<List<VoucherModel>> findAllByIdIn(List<String> ids);

}
