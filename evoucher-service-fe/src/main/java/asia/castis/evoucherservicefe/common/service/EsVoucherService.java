package asia.castis.evoucherservicefe.common.service;

import asia.castis.evoucherservicefe.common.enums.EnumTransferStatus;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import asia.castis.evoucherservicefe.exceptions.ElasticSearchException;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.exceptions.NotFoundException;

import java.util.List;
import java.util.Set;

public interface EsVoucherService {
    VoucherModel findById(String ev) throws NotFoundException;
    List<VoucherModel> findAllByIdIn(List<String> evs) throws NotFoundException;
    void saveAll(List<VoucherModel> voucherModels);
    List<VoucherModel> getExpiredVouchers() throws ElasticSearchException;
    void updateVoucherStatusesAndExpireDate(List<String> voucherIds, EnumVoucherStatus newStatus) throws ElasticSearchException;
    void updateVoucherAndTransferStatuses(List<String> voucherIds, EnumVoucherStatus voucherStatus, EnumTransferStatus transferStatus)
            throws ElasticSearchException;

    void updateVoucherStatus(String ev, EnumVoucherStatus voucherStatus) throws NotFoundException;

    List<VoucherModel> getUnClaimedVouchers() throws ElasticSearchException;

    List<VoucherModel> getVouchersByOriginalId(List<String> originalIds) throws ElasticSearchException;

    void delete(Set<String> voucherIds);

    VoucherModel findBySerialNo(String serialNumber);

    VoucherModel save(VoucherModel voucherModel);
}
