package asia.castis.web_hook.repository;

import asia.castis.web_hook.bean.entity.Voucher;
import asia.castis.web_hook.common.SystemType;
import asia.castis.web_hook.common.VoucherStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String>, JpaSpecificationExecutor<Voucher> {

    Optional<Voucher> findById(@Valid @NotNull String ev);

    Optional<List<Voucher>> findAllByVoucherStatusCodeInAndSystem(List<VoucherStatusCode> status, SystemType systemType);
    Optional<List<Voucher>> findAllByVoucherStatusCodeInAndSystemAndIdIn(List<VoucherStatusCode> status, SystemType systemType, List<String> ids);

    List<Voucher> findAllByExtPinIdAndSystemAndVoucherStatusCodeIn(Long pinId, SystemType systemType, List<VoucherStatusCode> usableStatus);
}