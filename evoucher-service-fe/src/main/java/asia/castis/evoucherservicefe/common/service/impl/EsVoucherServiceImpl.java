package asia.castis.evoucherservicefe.common.service.impl;

import asia.castis.evoucherservicefe.common.enums.EnumTransferStatus;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import asia.castis.evoucherservicefe.common.utils.LocalDateUtils;
import asia.castis.evoucherservicefe.exceptions.ElasticSearchException;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.service.EsVoucherService;
import asia.castis.evoucherservicefe.common.repository.VoucherRepository;
import asia.castis.evoucherservicefe.exceptions.NotFoundException;
import asia.castis.evoucherservicefe.exceptions.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("esVoucherService")
@Slf4j
public class EsVoucherServiceImpl implements EsVoucherService {
    private ElasticsearchRestTemplate elasticsearchTemplate;
    private ElasticsearchOperations elasticsearchOperations;

    private final VoucherRepository voucherRepository;
    @Value("${elastic.voucher.indexName}")
    private String indexName;
    @Value("${quartz.expireVoucher.voucherDurationInDays}")
    public int voucherDurationInDays;
    @Value("${quartz.restoreVoucher.confirmWaitDurationInDays}")
    public int confirmWaitDurationInDays;

    @Autowired
    public EsVoucherServiceImpl(ElasticsearchRestTemplate elasticsearchTemplate,
                                ElasticsearchOperations elasticsearchOperations,
                                VoucherRepository voucherRepository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.elasticsearchOperations = elasticsearchOperations;
        this.voucherRepository = voucherRepository;
    }

    @Override
    public VoucherModel findById(String ev) throws NotFoundException {
        return voucherRepository.findById(ev).orElseThrow(() -> new NotFoundException("can not find voucher"));
    }

    @Override
    public List<VoucherModel> findAllByIdIn(List<String> evs) throws NotFoundException {
        try {
            return voucherRepository.findAllByIdIn(evs).orElseThrow(() -> {
                log.error("could not find voucher by evs in: {}", evs);
                return new NotFoundException("can not find voucher id in " + evs);
            });
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new ServerException(e.getMessage());
        }
    }

    @Override
    public void saveAll(List<VoucherModel> voucherModels) {
        List<IndexQuery> indexQueries = new ArrayList<>();
        for (VoucherModel voucher : voucherModels) {
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withIndex(indexName)
                    .withId(voucher.getId())
                    .withObject(voucher)
                    .build();
            indexQueries.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(indexQueries, VoucherModel.class);
        log.info("All vouchers saved, size={}", voucherModels.size());
    }

    /***
     * <pre>
     * Expired voucher is
     * - Voucher status = NORMAL
     * - Has been created for 2 days or earlier
     * </pre>
     * @return
     * @throws ElasticSearchException
     */
    @Override
    public List<VoucherModel> getExpiredVouchers() {
        LocalDateTime milestone = LocalDateTime.now().minusDays(voucherDurationInDays);
        List<VoucherModel> results = voucherRepository.findExpiredVouchers(LocalDateUtils.toString(milestone));
        return results;
    }

    @Override
    public void updateVoucherStatusesAndExpireDate(List<String> voucherIds, EnumVoucherStatus newStatus) {
        voucherIds.forEach(voucherId -> {
            VoucherModel voucher = elasticsearchOperations.get(voucherId, VoucherModel.class);
            if (voucher != null) {
                voucher.setVoucherStatus(newStatus);
                voucher.setExpireDate(new Date());
                elasticsearchOperations.save(voucher);
            }
        });
        log.info("All vouchers status updated, status={}, size={}, ids={}, ", newStatus, voucherIds.size(), String.join(",", voucherIds));
    }

    @Override
    public void updateVoucherAndTransferStatuses(List<String> voucherIds, EnumVoucherStatus voucherStatus, EnumTransferStatus transferStatus) {
        voucherIds.forEach(voucherId -> {
            VoucherModel voucher = elasticsearchOperations.get(voucherId, VoucherModel.class);
            if (voucher != null) {
                voucher.setVoucherStatus(voucherStatus);
                voucher.setTransferStatus(transferStatus);
                elasticsearchOperations.save(voucher);
            }
        });
        log.info("All vouchers status updated, status={}, transferStatus={}, size={}, ids={}, ",
                voucherStatus, transferStatus, voucherIds.size(), String.join(",", voucherIds));
    }

    @Override
    public void updateVoucherStatus(String ev, EnumVoucherStatus voucherStatus) throws NotFoundException {
        VoucherModel voucher = elasticsearchOperations.get(ev, VoucherModel.class);
        if (voucher == null) {
            throw new NotFoundException(ev);
        }
        voucher.setVoucherStatus(voucherStatus);
        elasticsearchOperations.save(voucher);
    }

    /***
     * <pre>
     * Unclaimed voucher is
     * - Transfer status = RECPT_WAIT
     * - Has been created for 2 days or earlier
     * </pre>
     *
     * @return
     * @throws ElasticSearchException
     */
    @Override
    public List<VoucherModel> getUnClaimedVouchers() {
        LocalDateTime milestone = LocalDateTime.now().minusDays(confirmWaitDurationInDays);
        List<VoucherModel> results = voucherRepository.findUnClaimedVouchers(LocalDateUtils.toString(milestone));
        return results;
    }

    @Override
    public List<VoucherModel> getVouchersByOriginalId(List<String> originalIds) {
        Criteria criteria = new Criteria("originalVoucherId").in(originalIds);
        Query query = new CriteriaQuery(criteria);
        return elasticsearchOperations.search(query, VoucherModel.class).stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Set<String> voucherIds) {
        log.info("Delete vouchers={}", voucherIds.stream().collect(Collectors.joining(",")));
        voucherRepository.deleteAllById(voucherIds);
    }

    @Override
    public VoucherModel findBySerialNo(String serialNumber) {
        return voucherRepository.findBySerialNo(serialNumber);
    }

    @Override
    public VoucherModel save(VoucherModel voucherModel) {
        log.info("Save voucher, voucher={}", voucherModel);
        return voucherRepository.save(voucherModel);
    }
}