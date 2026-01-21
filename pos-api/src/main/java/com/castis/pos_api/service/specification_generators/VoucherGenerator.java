package com.castis.pos_api.service.specification_generators;

import com.castis.pos_api.entity.Voucher;
import com.castis.pos_api.enum_constant.TransferStatusCode;
import com.castis.pos_api.enum_constant.VoucherStatusCode;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

public class VoucherGenerator {
    public static Specification<Voucher> externalPinIsAndTransferStatusIsNullOrIn(
            @NotBlank String externalPinNo,
            @NotEmpty Collection<TransferStatusCode> otherTransferStatuses) {
        return (root, query, criteriaBuilder) -> {
            Predicate externalPinNoPredicate = criteriaBuilder.equal(root.get("extPinNo"), externalPinNo);
            CriteriaBuilder.In<String> transferStatusIn = criteriaBuilder.in(root.get("transferStatusCode"));
            for (TransferStatusCode status : otherTransferStatuses) {
                transferStatusIn.value(status.name());
            }
            Predicate transferStatusOrNul = criteriaBuilder.or(transferStatusIn,
                    criteriaBuilder.isNull(root.get("transferStatusCode")));
            return criteriaBuilder.and(
                    externalPinNoPredicate,
                    transferStatusOrNul
            );
        };
    }
}
