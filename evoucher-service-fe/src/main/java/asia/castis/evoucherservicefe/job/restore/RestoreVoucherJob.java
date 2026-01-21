package asia.castis.evoucherservicefe.job.restore;

import asia.castis.evoucherservicefe.common.enums.EnumTransferStatus;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherJobType;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.common.service.EsVoucherService;
import asia.castis.evoucherservicefe.common.utils.LocalDateUtils;
import asia.castis.evoucherservicefe.exceptions.ElasticSearchException;
import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import asia.castis.evoucherservicefe.job.dto.VoucherJobResultDto;
import asia.castis.evoucherservicefe.job.sender.JobResultSender;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestoreVoucherJob implements Job {

    @Autowired
    private EsVoucherService esVoucherService;
    @Autowired
    private JobResultSender jobResultSender;
    @Value("${quartz.expireVoucher.retryCount}")
    private int retryCount;
    @Value("${quartz.restoreVoucher.enabled}")
    private boolean restoreVoucherEnabled;
    public void execute(JobExecutionContext jobExecutionContext) {
        if (!restoreVoucherEnabled) {
            log.info("[RestoreVoucherJob] disabled");
            return;
        }
        int count = 1;
        do {
            log.info("[RestoreVoucherJob-{}nd try] start processing", count);
            try {
                run();
                log.info("[RestoreVoucherJob-{}nd try] finish processing", count);
                return;
            } catch (Exception e) {
                log.error(String.format("[RestoreVoucherJob-%d exception]. msg=%s. cause: %s",
                        count, e.getMessage(), e.getCause().getMessage()), e);
            }
            count++;
            if (count == retryCount) {
                log.error("[RestoreVoucherJob-{}nd try] Ran out of retries. Terminate job", count);
            }
        } while (count <= retryCount);
    }

    private void run() throws ElasticSearchException, SendMessageToQueueException {
        // Retrieve data from DB
        List<VoucherModel> unclaimedVouchers = esVoucherService.getUnClaimedVouchers();
        if (unclaimedVouchers.isEmpty()) {
            log.info("No voucher found");
            return;
        }
        log.info("Found {} unclaimed vouchers", unclaimedVouchers.size());
        // Find all original vouchers
        List<String> unclaimedVoucherIds = unclaimedVouchers.stream().map(VoucherModel::getId).collect(Collectors.toList());
        List<String> originalVoucherIds = unclaimedVouchers.stream().map(VoucherModel::getOriginalVoucherId).collect(Collectors.toList());
        log.info("Unclaimed vouchers={}", String.join(",", unclaimedVoucherIds));
        log.info("Original vouchers={}", String.join(",", originalVoucherIds));
        // Push to ES
        // Update unclaimed vouchers
        esVoucherService.updateVoucherAndTransferStatuses(unclaimedVoucherIds, EnumVoucherStatus.DISABLED, EnumTransferStatus.RETURN);
        // Update original vouchers
        if (originalVoucherIds.isEmpty()) {
            log.warn("No original voucher found");
        } else {
            esVoucherService.updateVoucherAndTransferStatuses(originalVoucherIds, EnumVoucherStatus.NORMAL, null);
        }
        // Send to Queue
        VoucherJobResultDto jobResult = new VoucherJobResultDto();
        jobResult.setRequestType(EnumVoucherJobType.RETURN.name());
        jobResult.setRequestDate(LocalDateUtils.getCurrentDateString());
        jobResult.setVoucherIds(unclaimedVoucherIds);
        jobResultSender.sendJobResult(jobResult);
    }
}
