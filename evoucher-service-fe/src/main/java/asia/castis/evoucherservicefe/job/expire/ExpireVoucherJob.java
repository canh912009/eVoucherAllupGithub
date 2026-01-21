package asia.castis.evoucherservicefe.job.expire;

import asia.castis.evoucherservicefe.common.enums.EnumVoucherStatus;
import asia.castis.evoucherservicefe.common.enums.EnumVoucherJobType;
import asia.castis.evoucherservicefe.common.service.EsVoucherService;
import asia.castis.evoucherservicefe.common.utils.DateUtils;
import asia.castis.evoucherservicefe.common.utils.LocalDateUtils;
import asia.castis.evoucherservicefe.exceptions.ElasticSearchException;
import asia.castis.evoucherservicefe.common.model.VoucherModel;
import asia.castis.evoucherservicefe.exceptions.SendMessageToQueueException;
import asia.castis.evoucherservicefe.job.dto.VoucherJobResultDto;
import asia.castis.evoucherservicefe.job.sender.JobResultSender;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ExpireVoucherJob implements Job {

    @Autowired
    private EsVoucherService esVoucherService;
    @Autowired
    private JobResultSender jobResultSender;

    @Value("${quartz.expireVoucher.retryCount}")
    private int retryCount;
    @Value("${quartz.expireVoucher.voucherDurationInDays}")
    public int voucherDurationInDays;
    @Value("${quartz.expireVoucher.enabled}")
    private boolean expireVoucherEnabled;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (!expireVoucherEnabled) {
            log.info("[ExpireVoucherJob] disabled");
            return;
        }
        int count = 1;
        do {
            log.info("[ExpireVoucherJob-{}nd try] start processing", count);
            try {
                run();
                log.info("[ExpireVoucherJob-{}nd try] finish processing", count);
                return;
            } catch (Exception e) {
                log.error(String.format("[ExpireVoucherJob-%d exception]. msg=%s. cause: %s",
                        count, e.getMessage(), e.getCause().getMessage()), e);
            }
            count++;
            if (count == retryCount) {
                log.error("[ExpireVoucherJob-{}nd try] Ran out of retries. Terminate job", count);
            }
        } while (count<= retryCount);
    }

    public void run() throws ElasticSearchException, SendMessageToQueueException {
        // Retrieve data from DB
        log.info("Finding vouchers need to be expired within {} days", voucherDurationInDays);
        List<VoucherModel> expiredVouchers = esVoucherService.getExpiredVouchers();
        if (expiredVouchers.isEmpty()) {
            log.info("No voucher found");
            return;
        }
        log.info("Found {} vouchers need to be expired", expiredVouchers.size());
        StringBuilder expireVoucherLogBuilder = new StringBuilder();
        for (VoucherModel voucher : expiredVouchers) {
            expireVoucherLogBuilder.append(String.format("{Id=%s, status=%s, createDate=%s}\r\n",
                    voucher.getId(), voucher.getVoucherStatus(), DateUtils.toDateTimeString(voucher.getCreateDate())));
        }
        log.info("{}", expireVoucherLogBuilder);
        // Update Voucher status
        for (VoucherModel expiredVoucher : expiredVouchers) {
            expiredVoucher.setExpireDate(new Date());
            expiredVoucher.setVoucherStatus(EnumVoucherStatus.EXPIRE);
        }
        // Push to ES
        List<String> voucherIds = expiredVouchers.stream().map(voucher -> voucher.getId()).collect(Collectors.toList());
        esVoucherService.updateVoucherStatusesAndExpireDate(voucherIds, EnumVoucherStatus.EXPIRE);
        // Send to Queue
        VoucherJobResultDto jobResult = new VoucherJobResultDto();
        jobResult.setRequestType(EnumVoucherJobType.EXPIRE.name());
        jobResult.setRequestDate(LocalDateUtils.getCurrentDateString());
        jobResult.setVoucherIds(voucherIds);
        jobResultSender.sendJobResult(jobResult);
    }
}
