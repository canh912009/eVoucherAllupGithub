package asia.castis.evoucher.api.elastic.repository;

import asia.castis.evoucher.api.elastic.model.VoucherModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface VoucherEsRepository extends ElasticsearchRepository<VoucherModel, String> {
    List<VoucherModel> findAllByChoiceVoucherEv(String choiceEv);
    List<VoucherModel> findAllByChoiceVoucherEvOrderByCreateDateDesc(String choiceEv);
    VoucherModel findBySerialNo(@NotNull String serialNumber);
}
